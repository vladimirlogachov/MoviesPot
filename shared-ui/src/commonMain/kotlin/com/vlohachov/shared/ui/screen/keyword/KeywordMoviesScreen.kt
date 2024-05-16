package com.vlohachov.shared.ui.screen.keyword

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

            KeywordMoviesScreen(
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

@Composable
internal expect fun KeywordMoviesScreen(
    keyword: String,
    keywordId: Int,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    viewModel: KeywordMoviesViewModel = koinInject { parametersOf(keywordId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
)

internal object KeywordMoviesDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}
