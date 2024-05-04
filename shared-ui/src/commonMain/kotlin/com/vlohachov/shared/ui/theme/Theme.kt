package com.vlohachov.shared.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * The default values used for [MoviesPotTheme].
 */
public object MoviesPotThemeDefaults {

    /**
     * The default [ColorScheme] used for [MoviesPotTheme].
     */
    public val colorScheme: ColorScheme
        @Composable get() = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    /**
     * The default [Shapes] used for [MoviesPotTheme].
     */
    public val shapes: Shapes
        @Composable get() = MaterialTheme.shapes

    /**
     * The default [Typography] used for [MoviesPotTheme].
     */
    public val typography: Typography
        @Composable get() = MaterialTheme.typography

}

/**
 * A [MoviesPotTheme] that uses the provided [colorScheme], [shapes], and [typography].
 */
@Composable
public fun MoviesPotTheme(
    colorScheme: ColorScheme = MoviesPotThemeDefaults.colorScheme,
    shapes: Shapes = MoviesPotThemeDefaults.shapes,
    typography: Typography = MoviesPotThemeDefaults.typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}

/**
 * A [MoviesPotTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
public expect fun MoviesPotTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
)
