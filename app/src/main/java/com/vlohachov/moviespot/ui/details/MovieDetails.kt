package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.bar.AppBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.destinations.CastDestination
import com.vlohachov.moviespot.ui.destinations.CrewDestination
import com.vlohachov.moviespot.ui.destinations.FullscreenImageDestination
import com.vlohachov.moviespot.ui.destinations.KeywordMoviesDestination
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import com.vlohachov.moviespot.ui.destinations.SimilarMoviesDestination
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MovieDetails(
    navigator: DestinationsNavigator,
    movieId: Long,
    movieTitle: String,
    viewModel: MovieDetailsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()
    ErrorBar(
        error = uiState.error,
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
                onBackClick = navigator::navigateUp,
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = MovieDetailsDefaults.ErrorBarTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .semantics {
                    testTag = MovieDetailsDefaults.DetailsContentTestTag
                }
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            movieId = movieId,
            movieTitle = movieTitle,
            viewState = uiState,
            navigator = navigator,
            onError = viewModel::onError,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    movieId: Long,
    movieTitle: String,
    viewState: MovieDetailsViewState,
    navigator: DestinationsNavigator,
    onError: (error: Throwable) -> Unit,
) = with(viewState) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        details(
            director = if (directorViewState is ViewState.Success) {
                directorViewState.data
            } else {
                ""
            },
            detailsViewState = detailsViewState,
            onPoster = { path -> navigator.navigate(FullscreenImageDestination(path = path)) },
            onCast = { navigator.navigate(CastDestination(movieId = movieId)) },
            onCrew = { navigator.navigate(CrewDestination(movieId = movieId)) },
            onError = onError,
        )
        item {
            MoviesSection(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.recommendations),
                viewState = recommendationsViewState,
                onMore = {
                    navigator.navigate(
                        SimilarMoviesDestination(movieId = movieId, movieTitle = movieTitle)
                    )
                },
                onMovieClick = { movie ->
                    navigator.navigate(
                        MovieDetailsDestination(
                            movieId = movie.id,
                            movieTitle = movie.title,
                        )
                    )
                },
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium
                ),
            )
        }
        keywords(
            onKeyword = { keyword ->
                navigator.navigate(
                    KeywordMoviesDestination(
                        keywordId = keyword.id,
                        keyword = keyword.name,
                    )
                )
            },
            viewState = keywordsViewState,
        )
    }
}

private fun LazyListScope.details(
    director: String,
    detailsViewState: ViewState<MovieDetails>,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    when (detailsViewState) {
        ViewState.Loading -> item {
            CircularProgressIndicator(
                modifier = Modifier
                    .semantics {
                        testTag = MovieDetailsDefaults.DetailsLoadingTestTag
                    }
                    .padding(all = 16.dp)
            )
        }

        is ViewState.Error -> detailsViewState.error?.run(onError)
        is ViewState.Success -> with(detailsViewState.data) {
            item {
                Headline(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.HeadlineTestTag
                        }
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    director = director,
                    details = this@with,
                    onPosterClick = onPoster,
                )
            }
            if (tagline.isNotBlank()) {
                item {
                    Text(
                        modifier = Modifier
                            .semantics {
                                testTag = MovieDetailsDefaults.TaglineTestTag
                            }
                            .padding(horizontal = 16.dp),
                        text = "\"$tagline\"",
                    )
                }
            }
            item {
                ShortSummary(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.ShortSummaryTestTag
                        }
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    isAdult = isAdult,
                    runtime = runtime,
                )
            }
            item {
                Credits(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onCast = onCast,
                    onCrew = onCrew,
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
            item {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
            item {
                Overview(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.OverviewTestTag
                        }
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    text = overview,
                )
            }
            item {
                Production(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.ProductionTestTag
                        }
                        .fillMaxWidth(),
                    companies = productionCompanies,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.keywords(
    onKeyword: (keyword: Keyword) -> Unit,
    viewState: ViewState<List<Keyword>>,
) {
    if (viewState is ViewState.Success) {
        item {
            Section(
                modifier = Modifier
                    .semantics {
                        testTag = MovieDetailsDefaults.KeywordsTestTag
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = {
                    SectionTitle(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.keywords),
                    )
                },
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium,
                ),
            ) {
                if (viewState.data.isEmpty()) {
                    Text(text = stringResource(id = R.string.no_results))
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                    ) {
                        for (keyword in viewState.data) {
//                            ClickableText(
//                                modifier = Modifier
//                                    .border(
//                                        width = 1.dp,
//                                        color = MaterialTheme.colorScheme.outline,
//                                        shape = SuggestionChipDefaults.shape,
//                                    )
//                                    .padding(vertical = 8.dp, horizontal = 16.dp),
//                                text = buildAnnotatedString { keyword.name },
//                                style = MaterialTheme.typography.labelMedium,
//                                onClick = { onKeyword(keyword) },
//                            )
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


object MovieDetailsDefaults {

    const val BackButtonTestTag = "back_button"
    const val ErrorBarTestTag = "error_bar"
    const val DetailsContentTestTag = "details_content"
    const val DetailsLoadingTestTag = "details_loading"
    const val HeadlineTestTag = "movie_headline"
    const val TaglineTestTag = "movie_tagline"
    const val ShortSummaryTestTag = "movie_short_summary"
    const val CastButtonTestTag = "movie_cast"
    const val CrewButtonTestTag = "movie_crew"
    const val OverviewTestTag = "movie_overview"
    const val ProductionTestTag = "movie_production"
    const val KeywordsTestTag = "movie_keywords"
}
