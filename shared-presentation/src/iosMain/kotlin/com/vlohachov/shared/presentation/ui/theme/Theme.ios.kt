package com.vlohachov.shared.presentation.ui.theme

import androidx.compose.runtime.Composable

/**
 * A [MoviesPotTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
internal actual fun MoviesPotTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    MoviesPotTheme(content = content)
}

/**
 * Returns whether the dynamic theme is available.
 */
internal actual fun isDynamicThemeAvailable(): Boolean = false
