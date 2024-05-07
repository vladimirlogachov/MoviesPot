package com.vlohachov.shared.ui.screen.movies

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

internal data object MoviesScreen : Screen {

    private const val ArgCategory = "category"

    private val arguments = listOf(
        navArgument(name = ArgCategory) { type = NavType.StringType }
    )

    override val path: String = "movies"

    @OptIn(ExperimentalMaterial3Api::class)
    fun NavGraphBuilder.movies(navController: NavController) {
        composable(route = "$path/{$ArgCategory}", arguments = arguments) { backStackEntry ->
            val category =
                requireNotNull(value = backStackEntry.arguments?.getString(ArgCategory)) {
                    "Missing required argument $ArgCategory"
                }.run(MovieCategory::valueOf)

            Movies(
                category = category,
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    navController.navigate(route = "${MovieDetailsScreen.path}/${movie.id}")
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
