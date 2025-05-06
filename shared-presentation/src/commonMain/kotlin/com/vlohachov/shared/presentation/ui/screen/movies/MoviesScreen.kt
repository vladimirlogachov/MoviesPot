package com.vlohachov.shared.presentation.ui.screen.movies

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.presentation.core.collectAsLazyPagingItems
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTop
import com.vlohachov.shared.presentation.ui.component.movie.MoviesPaginatedGrid
import com.vlohachov.shared.presentation.ui.screen.Screen
import com.vlohachov.shared.presentation.ui.screen.details.MovieDetailsScreen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.now_playing
import moviespot.shared_presentation.generated.resources.popular
import moviespot.shared_presentation.generated.resources.top_rated
import moviespot.shared_presentation.generated.resources.upcoming
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

internal data object MoviesScreen : Screen<MoviesScreen.Params>() {

    internal data class Params(val category: MovieCategory)

    private const val ArgCategory = "category"

    override val path: String = "movies/{$ArgCategory}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgCategory) { type = NavType.StringType }
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgCategory}", newValue = "${params.category}")

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val category = backStackEntry.arguments.readOrThrow(
                block = { getString(ArgCategory) },
                lazyMessage = { "Missing required argument $ArgCategory" },
            ).run(MovieCategory::valueOf)

            Movies(
                category = category,
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    MovieDetailsScreen.Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
                }
            )
        }
    }

}

private const val VISIBLE_ITEMS_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun Movies(
    category: MovieCategory,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    viewModel: MoviesViewModel = koinViewModel { parametersOf(category) },
    gridState: LazyGridState = rememberLazyGridState(),
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val showScrollToTop by remember {
        derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD }
    }
    val error by viewModel.error.collectAsState()
    ErrorBar(
        error = error,
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
                title = stringResource(resource = category.titleRes()),
                scrollBehavior = scrollBehavior,
                onBackClick = onBack,
            )
        },
        floatingActionButton = {
            ScrollToTop(
                modifier = Modifier.navigationBarsPadding(),
                visible = showScrollToTop,
                gridState = gridState
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
        MoviesPaginatedGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            columns = GridCells.Fixed(count = 3),
            movies = viewModel.movies.collectAsLazyPagingItems(),
            onClick = onMovieDetails,
            onError = viewModel::onError,
            state = gridState,
        )
    }
}

private fun MovieCategory.titleRes(): StringResource = when (this) {
    MovieCategory.NOW_PLAYING -> Res.string.now_playing
    MovieCategory.POPULAR -> Res.string.popular
    MovieCategory.TOP_RATED -> Res.string.top_rated
    MovieCategory.UPCOMING -> Res.string.upcoming
}
