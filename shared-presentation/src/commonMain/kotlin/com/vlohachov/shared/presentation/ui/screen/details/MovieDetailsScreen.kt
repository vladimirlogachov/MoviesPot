package com.vlohachov.shared.presentation.ui.screen.details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.presentation.core.ScopeExtension
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.movie.MoviesSection
import com.vlohachov.shared.presentation.ui.component.section.Section
import com.vlohachov.shared.presentation.ui.component.section.SectionDefaults
import com.vlohachov.shared.presentation.ui.component.section.SectionTitle
import com.vlohachov.shared.presentation.ui.screen.Screen
import com.vlohachov.shared.presentation.ui.screen.credits.cast.CastScreen
import com.vlohachov.shared.presentation.ui.screen.credits.crew.CrewScreen
import com.vlohachov.shared.presentation.ui.screen.image.FullscreenImageScreen
import com.vlohachov.shared.presentation.ui.screen.keyword.KeywordMoviesScreen
import com.vlohachov.shared.presentation.ui.screen.movies.similar.SimilarMoviesScreen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.keywords
import moviespot.shared_presentation.generated.resources.no_results
import moviespot.shared_presentation.generated.resources.recommendations
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

internal data object MovieDetailsScreen : Screen<MovieDetailsScreen.Params>() {

    internal data class Params(val movieId: Long, val movieTitle: String)

    private const val ArgMovieId = "movieId"
    private const val ArgMovieTitle = "movieTitle"

    override val path: String = "movie/{$ArgMovieId}?$ArgMovieTitle={$ArgMovieTitle}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgMovieId) { type = NavType.LongType },
        navArgument(name = ArgMovieTitle) { type = NavType.StringType }
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgMovieId}", newValue = params.movieId.toString())
            .replace(oldValue = "{$ArgMovieTitle}", newValue = params.movieTitle)

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val movieId = backStackEntry.arguments.readOrThrow(
                block = { getLong(ArgMovieId) },
                lazyMessage = { "Missing required argument $ArgMovieId" },
            )
            val movieTitle = backStackEntry.arguments.readOrThrow(
                block = { getString(ArgMovieTitle) },
                lazyMessage = { "Missing required argument $ArgMovieTitle" },
            )

            MovieDetails(
                movieId = movieId,
                onBack = navController::navigateUp,
                onFullscreenImage = { imagePath ->
                    FullscreenImageScreen.Params(path = imagePath)
                        .run(FullscreenImageScreen::route)
                        .run(navController::navigate)
                },
                onCast = {
                    CastScreen.Params(movieId = movieId)
                        .run(CastScreen::route)
                        .run(navController::navigate)
                },
                onCrew = {
                    CrewScreen.Params(movieId = movieId)
                        .run(CrewScreen::route)
                        .run(navController::navigate)
                },
                onSimilar = {
                    SimilarMoviesScreen.Params(movieId = movieId, movieTitle = movieTitle)
                        .run(SimilarMoviesScreen::route)
                        .run(navController::navigate)
                },
                onMovieDetails = { movie ->
                    Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
                },
                onKeywordMovies = { keyword ->
                    KeywordMoviesScreen.Params(keywordId = keyword.id, keyword = keyword.name)
                        .run(KeywordMoviesScreen::route)
                        .run(navController::navigate)
                },
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun MovieDetails(
    movieId: Long,
    onBack: () -> Unit,
    onFullscreenImage: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onSimilar: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    onKeywordMovies: (keyword: Keyword) -> Unit,
    viewModel: MovieDetailsViewModel = koinViewModel { parametersOf(movieId) },
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    ErrorBar(
        error = uiState.error,
        duration = snackbarDuration,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "",
                scrollBehavior = scrollBehavior,
                onBackClick = onBack,
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = ErrorBarDefaults.ErrorTestTag)
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .testTag(tag = MovieDetailsDefaults.DetailsContentTestTag)
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            viewState = uiState,
            onFullscreenImage = onFullscreenImage,
            onCast = onCast,
            onCrew = onCrew,
            onSimilar = onSimilar,
            onMovieDetails = onMovieDetails,
            onKeywordMovies = onKeywordMovies,
            onError = viewModel::onError,
        )
    }
}

@Composable
private fun Content(
    viewState: MovieDetailsViewState,
    onFullscreenImage: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onSimilar: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    onKeywordMovies: (keyword: Keyword) -> Unit,
    onError: (error: Throwable) -> Unit,
    modifier: Modifier = Modifier,
) = with(viewState) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        details(
            directorViewState = directorViewState,
            detailsViewState = detailsViewState,
            onFullscreenImage = onFullscreenImage,
            onCast = onCast,
            onCrew = onCrew,
            onError = onError,
        )
        item {
            MoviesSection(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(resource = Res.string.recommendations),
                viewState = recommendationsViewState,
                onMore = onSimilar,
                onMovieClick = onMovieDetails,
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium
                ),
            )
        }
        keywords(onKeyword = onKeywordMovies, viewState = keywordsViewState)
    }
}

@ScopeExtension
private fun LazyListScope.details(
    directorViewState: ViewState<String>,
    detailsViewState: ViewState<MovieDetails>,
    onFullscreenImage: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onError: (Throwable) -> Unit,
) {
    when (detailsViewState) {
        ViewState.Loading -> item {
            CircularProgressIndicator(
                modifier = Modifier
                    .testTag(tag = MovieDetailsDefaults.DetailsLoadingTestTag)
                    .padding(all = 16.dp)
            )
        }

        is ViewState.Success -> item {
            Details(
                modifier = Modifier.fillMaxWidth(),
                director = if (directorViewState is ViewState.Success) {
                    directorViewState.data
                } else {
                    ""
                },
                details = detailsViewState.data,
                onPoster = onFullscreenImage,
                onCast = onCast,
                onCrew = onCrew,
            )
        }

        is ViewState.Error -> item {
            LaunchedEffect(key1 = detailsViewState.error) {
                detailsViewState.error?.run(onError)
            }
        }
    }
}

@Composable
private fun Details(
    modifier: Modifier,
    director: String,
    details: MovieDetails,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
) = with(details) {
    Column(modifier = modifier) {
        Headline(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            director = director,
            details = this@with,
            onPosterClick = onPoster,
        )
        with(tagline) {
            if (isNotBlank()) {
                Text(
                    modifier = Modifier
                        .testTag(tag = MovieDetailsDefaults.TaglineTestTag)
                        .padding(horizontal = 16.dp),
                    text = "\"$this\"",
                )
            }
        }
        ShortSummary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            voteAverage = voteAverage,
            voteCount = voteCount,
            isAdult = isAdult,
            runtime = runtime,
        )
        Credits(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onCast = onCast,
            onCrew = onCrew,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Overview(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            text = overview,
        )
        Production(
            modifier = Modifier.fillMaxWidth(),
            companies = productionCompanies,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@ScopeExtension
private fun LazyListScope.keywords(
    onKeyword: (keyword: Keyword) -> Unit,
    viewState: ViewState<List<Keyword>>,
) {
    if (viewState is ViewState.Success) {
        item {
            Section(
                modifier = Modifier
                    .testTag(tag = MovieDetailsDefaults.KeywordsTestTag)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = {
                    SectionTitle(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(resource = Res.string.keywords),
                    )
                },
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium,
                ),
            ) {
                if (viewState.data.isEmpty()) {
                    Text(text = stringResource(resource = Res.string.no_results))
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                    ) {
                        for (keyword in viewState.data) {
                            Text(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = SuggestionChipDefaults.shape,
                                    )
                                    .clip(shape = SuggestionChipDefaults.shape)
                                    .clickable { onKeyword(keyword) }
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                text = keyword.name,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}

internal object MovieDetailsDefaults {

    const val DetailsContentTestTag = "details_content"
    const val DetailsLoadingTestTag = "details_loading"
    const val TaglineTestTag = "movie_tagline"
    const val KeywordsTestTag = "movie_keywords"

}
