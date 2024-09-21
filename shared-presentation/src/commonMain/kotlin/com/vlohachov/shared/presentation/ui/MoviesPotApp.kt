package com.vlohachov.shared.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.presentation.di.appComponent
import com.vlohachov.shared.presentation.ui.screen.main.MainScreen
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
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
public fun MoviesPotApp(): Unit = KoinApplication(
    application = { modules(modules = appComponent) }
) {
    val preferences: LocalPreferences = koinInject()
    val applyDynamicTheme by preferences.applyDynamicThemeFlow
        .collectAsState(initial = false)
    val navController = rememberNavController()

    MoviesPotTheme(dynamicColor = applyDynamicTheme) {
        NavHost(
            navController = navController,
            startDestination = MainScreen.route(params = Unit)
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
    }
}
