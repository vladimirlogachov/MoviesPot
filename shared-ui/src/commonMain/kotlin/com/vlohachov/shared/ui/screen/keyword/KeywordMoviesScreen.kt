package com.vlohachov.shared.ui.screen.keyword

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

internal data object KeywordMoviesScreen : Screen {

    private const val ArgKeyword = "keyword"
    private const val ArgKeywordId = "keywordId"

    private val arguments = listOf(
        navArgument(name = ArgKeyword) { type = NavType.StringType },
        navArgument(name = ArgKeywordId) { type = NavType.IntType },
    )

    override val path: String = "movie/{$ArgKeyword}?$ArgKeywordId={$ArgKeywordId}"

    fun path(keyword: String, keywordId: Int): String =
        "movie/$keyword?$ArgKeywordId=$keywordId"

    fun NavGraphBuilder.keywordMovies(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val keyword =
                requireNotNull(value = backStackEntry.arguments?.getString(ArgKeyword)) {
                    "Missing required argument $ArgKeyword"
                }
            val keywordId =
                requireNotNull(value = backStackEntry.arguments?.getInt(ArgKeywordId)) {
                    "Missing required argument $ArgKeywordId"
                }

            KeywordMoviesScreen(
                keyword = keyword,
                keywordId = keywordId,
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
internal expect fun KeywordMoviesScreen(
    keyword: String,
    keywordId: Int,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
)

internal expect val keywordMoviesModule: Module

internal object KeywordMoviesDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
