package com.vlohachov.shared.presentation.ui.screen.keyword

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
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
import com.vlohachov.shared.presentation.core.collectAsLazyPagingItems
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTop
import com.vlohachov.shared.presentation.ui.component.movie.MoviesPaginatedGrid
import com.vlohachov.shared.presentation.ui.screen.Screen
import com.vlohachov.shared.presentation.ui.screen.details.MovieDetailsScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

internal data object KeywordMoviesScreen : Screen<KeywordMoviesScreen.Params>() {

    internal data class Params(val keyword: String, val keywordId: Int)

    private const val ArgKeyword = "keyword"
    private const val ArgKeywordId = "keywordId"

    override val path: String = "movie/{$ArgKeyword}?$ArgKeywordId={$ArgKeywordId}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgKeyword) { type = NavType.StringType },
        navArgument(name = ArgKeywordId) { type = NavType.IntType },
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgKeyword}", newValue = params.keyword)
            .replace(oldValue = "{$ArgKeywordId}", newValue = params.keywordId.toString())

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val keyword =
                requireNotNull(value = backStackEntry.arguments?.getString(ArgKeyword)) {
                    "Missing required argument $ArgKeyword"
                }
            val keywordId =
                requireNotNull(value = backStackEntry.arguments?.getInt(ArgKeywordId)) {
                    "Missing required argument $ArgKeywordId"
                }

            KeywordMovies(
                keyword = keyword,
                keywordId = keywordId,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun KeywordMovies(
    keyword: String,
    keywordId: Int,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    viewModel: KeywordMoviesViewModel = koinInject { parametersOf(keywordId) },
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
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
                title = keyword,
                scrollBehavior = scrollBehavior,
                onBackClick = onBack
            )
        },
        floatingActionButton = {
            ScrollToTop(
                modifier = Modifier.navigationBarsPadding(),
                visible = showScrollToTop,
                gridState = gridState,
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
            state = gridState,
            columns = GridCells.Fixed(count = 3),
            movies = viewModel.movies.collectAsLazyPagingItems(),
            onClick = onMovieDetails,
            onError = viewModel::onError,
        )
    }
}
