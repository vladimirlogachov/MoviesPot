package com.vlohachov.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
public fun MoviesPotApp(): Unit = KoinContext {
    val preferences: LocalPreferences = koinInject()
    val applyDynamicTheme by preferences.applyDynamicThemeFlow
        .collectAsState(initial = false)

    MoviesPotTheme(dynamicColor = applyDynamicTheme) {

    }
}
