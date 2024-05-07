package com.vlohachov.shared.ui.screen.movies.similar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen
import org.koin.core.module.Module

internal data object SimilarMoviesScreen : Screen {

    private const val ArgMovieId = "movieId"
    private const val ArgMovieTitle = "movieTitle"

    private val arguments = listOf(
        navArgument(name = ArgMovieId) { type = NavType.LongType },
        navArgument(name = ArgMovieTitle) { type = NavType.StringType }
    )

    override val path: String = "movie/{$ArgMovieId}/similar?$ArgMovieTitle={$ArgMovieTitle}"

    fun path(movieId: Long, movieTitle: String): String =
        "movie/$movieId/similar?$ArgMovieTitle=$movieTitle"

    fun NavGraphBuilder.similarMovies(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val movieId =
                requireNotNull(value = backStackEntry.arguments?.getLong(ArgMovieId)) {
                    "Missing required argument $ArgMovieId"
                }
            val movieTitle =
                requireNotNull(value = backStackEntry.arguments?.getString(ArgMovieTitle)) {
                    "Missing required argument $ArgMovieTitle"
                }
            SimilarMovies(
                movieId = movieId,
                movieTitle = movieTitle,
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    navController.navigate(
                        route = MovieDetailsScreen.path(
                            movieId = movie.id,
                            movieTitle = movie.title
                        )
                    )
                }
            )
        }
    }

}

@Composable
internal expect fun SimilarMovies(
    movieId: Long,
    movieTitle: String,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
)

internal expect val similarMoviesModule: Module

internal object SimilarMoviesDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
