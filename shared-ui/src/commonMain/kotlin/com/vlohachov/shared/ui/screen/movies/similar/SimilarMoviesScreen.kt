package com.vlohachov.shared.ui.screen.movies.similar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

internal data object SimilarMoviesScreen : Screen<SimilarMoviesScreen.Params>() {

    internal data class Params(val movieId: Long, val movieTitle: String)

    private const val ArgMovieId = "movieId"
    private const val ArgMovieTitle = "movieTitle"

    override val path: String = "movie/{$ArgMovieId}/similar?$ArgMovieTitle={$ArgMovieTitle}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgMovieId) { type = NavType.LongType },
        navArgument(name = ArgMovieTitle) { type = NavType.StringType }
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgMovieId}", newValue = params.movieId.toString())
            .replace(oldValue = "{$ArgMovieTitle}", newValue = params.movieTitle)

    override fun NavGraphBuilder.composable(navController: NavController) {
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
                    MovieDetailsScreen.Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
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
    viewModel: SimilarMoviesViewModel = koinInject { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
)

internal object SimilarMoviesDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
