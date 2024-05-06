package com.vlohachov.shared.ui.theme

import androidx.compose.runtime.Composable

/**
 * A [MoviesPotTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
public actual fun MoviesPotTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    MoviesPotTheme(content = content)
}

/**
 * Returns whether the dynamic theme is available.
 */
internal actual fun isDynamicThemeAvailable(): Boolean = false
