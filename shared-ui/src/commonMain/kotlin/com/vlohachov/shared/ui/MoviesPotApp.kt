package com.vlohachov.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.ui.screen.main.MainScreen
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import com.vlohachov.shared.ui.screen.credits.cast.CastScreen.screen as cast
import com.vlohachov.shared.ui.screen.credits.crew.CrewScreen.screen as crew
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen.screen as movieDetails
import com.vlohachov.shared.ui.screen.discover.DiscoverScreen.screen as discoverMovies
import com.vlohachov.shared.ui.screen.discover.result.DiscoverResultScreen.screen as discoverResult
import com.vlohachov.shared.ui.screen.image.FullscreenImageScreen.screen as fullscreenImage
import com.vlohachov.shared.ui.screen.keyword.KeywordMoviesScreen.screen as keywordMovies
import com.vlohachov.shared.ui.screen.main.MainScreen.screen as mainScreen
import com.vlohachov.shared.ui.screen.movies.MoviesScreen.screen as movies
import com.vlohachov.shared.ui.screen.movies.similar.SimilarMoviesScreen.screen as similarMovies
import com.vlohachov.shared.ui.screen.search.MoviesSearchScreen.screen as moviesSearch
import com.vlohachov.shared.ui.screen.settings.SettingsScreen.screen as settingsScreen

@Composable
public fun MoviesPotApp(): Unit = KoinContext {
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
