package com.vlohachov.shared.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vlohachov.shared.presentation.ui.screen.main.MainScreen
import com.vlohachov.shared.presentation.ui.screen.credits.cast.CastScreen.composable as cast
import com.vlohachov.shared.presentation.ui.screen.credits.crew.CrewScreen.composable as crew
import com.vlohachov.shared.presentation.ui.screen.details.MovieDetailsScreen.composable as movieDetails
import com.vlohachov.shared.presentation.ui.screen.discover.DiscoverScreen.composable as discoverMovies
import com.vlohachov.shared.presentation.ui.screen.discover.result.DiscoverResultScreen.composable as discoverResult
import com.vlohachov.shared.presentation.ui.screen.image.FullscreenImageScreen.composable as fullscreenImage
import com.vlohachov.shared.presentation.ui.screen.keyword.KeywordMoviesScreen.composable as keywordMovies
import com.vlohachov.shared.presentation.ui.screen.main.MainScreen.composable as mainScreen
import com.vlohachov.shared.presentation.ui.screen.movies.MoviesScreen.composable as movies
import com.vlohachov.shared.presentation.ui.screen.movies.similar.SimilarMoviesScreen.composable as similarMovies
import com.vlohachov.shared.presentation.ui.screen.search.MoviesSearchScreen.composable as moviesSearch
import com.vlohachov.shared.presentation.ui.screen.settings.SettingsScreen.composable as settingsScreen

@Composable
internal fun MoviesPotNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainScreen.route(params = Unit),
) = NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination,
) {
    mainScreen(navController = navController)
    moviesSearch(navController = navController)
    settingsScreen(navController = navController)
    movies(navController = navController)
    discoverMovies(navController = navController)
    discoverResult(navController = navController)
    movieDetails(navController = navController)
    fullscreenImage(navController = navController)
    cast(navController = navController)
    crew(navController = navController)
    similarMovies(navController = navController)
    keywordMovies(navController = navController)
}
