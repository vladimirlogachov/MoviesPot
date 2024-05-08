package com.vlohachov.shared.ui.screen.search

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen

internal data object MoviesSearchScreen : Screen<Unit>() {

    override val path: String = "search"

    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.screen(navController: NavController) {
        composable(route = path, arguments = arguments) {
            MoviesSearch(
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    MovieDetailsScreen.Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
                },
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal expect fun MoviesSearch(
    onBack: () -> Unit,
    onMovieDetails: (Movie) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
)

internal object SearchMoviesDefaults {

    const val SearchFieldTestTag = "search_field"
    const val ClearSearchFieldTestTag = "clear_search_field"
    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
