package com.vlohachov.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.ui.screen.credits.cast.CastScreen.cast
import com.vlohachov.shared.ui.screen.credits.crew.CrewScreen.crew
import com.vlohachov.shared.ui.screen.details.MovieDetailsScreen.movieDetails
import com.vlohachov.shared.ui.screen.discover.DiscoverScreen.discoverMovies
import com.vlohachov.shared.ui.screen.discover.result.DiscoverResultScreen.discoverResult
import com.vlohachov.shared.ui.screen.image.FullscreenImageScreen.fullscreenImage
import com.vlohachov.shared.ui.screen.keyword.KeywordMoviesScreen.keywordMovies
import com.vlohachov.shared.ui.screen.main.MainScreen
import com.vlohachov.shared.ui.screen.main.MainScreen.mainScreen
import com.vlohachov.shared.ui.screen.movies.MoviesScreen.movies
import com.vlohachov.shared.ui.screen.movies.similar.SimilarMoviesScreen.similarMovies
import com.vlohachov.shared.ui.screen.search.MoviesSearchScreen.moviesSearch
import com.vlohachov.shared.ui.screen.settings.SettingsScreen.settingsScreen
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
public fun MoviesPotApp(): Unit = KoinContext {
    val preferences: LocalPreferences = koinInject()
    val applyDynamicTheme by preferences.applyDynamicThemeFlow
        .collectAsState(initial = false)
    val navController = rememberNavController()

    MoviesPotTheme(dynamicColor = applyDynamicTheme) {
        NavHost(navController = navController, startDestination = MainScreen.path) {
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
