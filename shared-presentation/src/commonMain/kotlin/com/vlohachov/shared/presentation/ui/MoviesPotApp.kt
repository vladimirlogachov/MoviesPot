package com.vlohachov.shared.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.presentation.ui.navigation.MoviesPotNavHost
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
public fun MoviesPotApp(): Unit = KoinContext {
    val applyDynamicTheme by koinInject<LocalPreferences>()
        .applyDynamicThemeFlow.collectAsState(initial = false)

    MoviesPotTheme(dynamicColor = applyDynamicTheme) {
        MoviesPotNavHost()
    }
}
