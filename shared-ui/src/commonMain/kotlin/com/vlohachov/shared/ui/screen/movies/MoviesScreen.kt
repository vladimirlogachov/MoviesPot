package com.vlohachov.shared.ui.screen.movies

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen
import org.koin.core.module.Module

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
    override fun NavGraphBuilder.screen(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val category =
                requireNotNull(value = backStackEntry.arguments?.getString(ArgCategory)) {
                    "Missing required argument $ArgCategory"
                }.run(MovieCategory::valueOf)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal expect fun Movies(
    category: MovieCategory,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
)

internal expect val moviesModule: Module

internal object MoviesDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
