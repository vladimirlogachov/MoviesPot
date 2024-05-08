package com.vlohachov.shared.ui.screen.discover.result

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

internal data object DiscoverResultScreen : Screen<DiscoverResultScreen.Params>() {

    internal data class Params(val year: Int?, val genres: List<Int>?)

    private const val ArgYear = "year"
    private const val ArgGenres = "genres"

    override val path: String =
        "discover/result?$ArgYear={$ArgYear}&$ArgGenres={$ArgGenres}"

    override val arguments = listOf(
        navArgument(name = ArgYear) { type = NavType.StringType; nullable = true },
        navArgument(name = ArgGenres) { type = NavType.StringType; nullable = true },
    )

    override fun route(params: Params): String =
        path.replace(
            oldValue = "{$ArgYear}",
            newValue = params.year.toString()
        ).replace(
            oldValue = "{$ArgGenres}",
            newValue = params.genres?.joinToString(separator = ",").toString()
        )

    override fun NavGraphBuilder.screen(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val year = backStackEntry.arguments?.getString(ArgYear)
            val genres = backStackEntry.arguments?.getString(ArgGenres)

            DiscoverResult(
                year = year?.toIntOrNull(),
                genres = genres?.parseGenres(),
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    MovieDetailsScreen.Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
                },
            )
        }
    }

    private fun String.parseGenres(): IntArray =
        split(",")
            .mapNotNull { item -> item.toIntOrNull() }
            .toIntArray()

}

@Composable
internal expect fun DiscoverResult(
    year: Int?,
    genres: IntArray?,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
)

internal expect val discoverResultModule: Module

internal object DiscoverResultDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
