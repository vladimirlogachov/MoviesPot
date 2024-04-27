package com.vlohachov.shared.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val md_theme_light_primary = Color(color = 0xFF006E08)
internal val md_theme_light_onPrimary = Color(color = 0xFFFFFFFF)
internal val md_theme_light_primaryContainer = Color(color = 0xFF97F986)
internal val md_theme_light_onPrimaryContainer = Color(color = 0xFF002201)
internal val md_theme_light_secondary = Color(color = 0xFF53634E)
internal val md_theme_light_onSecondary = Color(color = 0xFFFFFFFF)
internal val md_theme_light_secondaryContainer = Color(color = 0xFFD7E8CD)
internal val md_theme_light_onSecondaryContainer = Color(color = 0xFF121F0E)
internal val md_theme_light_tertiary = Color(color = 0xFF386569)
internal val md_theme_light_onTertiary = Color(color = 0xFFFFFFFF)
internal val md_theme_light_tertiaryContainer = Color(color = 0xFFBCEBEF)
internal val md_theme_light_onTertiaryContainer = Color(color = 0xFF002022)
internal val md_theme_light_error = Color(color = 0xFFBA1A1A)
internal val md_theme_light_errorContainer = Color(color = 0xFFFFDAD6)
internal val md_theme_light_onError = Color(color = 0xFFFFFFFF)
internal val md_theme_light_onErrorContainer = Color(color = 0xFF410002)
internal val md_theme_light_background = Color(color = 0xFFFCFDF6)
internal val md_theme_light_onBackground = Color(color = 0xFF1A1C18)
internal val md_theme_light_surface = Color(color = 0xFFFCFDF6)
internal val md_theme_light_onSurface = Color(color = 0xFF1A1C18)
internal val md_theme_light_surfaceVariant = Color(color = 0xFFDFE4D8)
internal val md_theme_light_onSurfaceVariant = Color(color = 0xFF43483F)
internal val md_theme_light_outline = Color(color = 0xFF73796E)
internal val md_theme_light_inverseOnSurface = Color(color = 0xFFF1F1EB)
internal val md_theme_light_inverseSurface = Color(color = 0xFF2F312D)
internal val md_theme_light_inversePrimary = Color(color = 0xFF7CDC6D)
internal val md_theme_light_shadow = Color(color = 0xFF000000)
internal val md_theme_light_surfaceTint = Color(color = 0xFF006E08)

internal val md_theme_dark_primary = Color(color = 0xFF7CDC6D)
internal val md_theme_dark_onPrimary = Color(color = 0xFF003A02)
internal val md_theme_dark_primaryContainer = Color(color = 0xFF005304)
internal val md_theme_dark_onPrimaryContainer = Color(color = 0xFF97F986)
internal val md_theme_dark_secondary = Color(color = 0xFFBBCBB2)
internal val md_theme_dark_onSecondary = Color(color = 0xFF263422)
internal val md_theme_dark_secondaryContainer = Color(color = 0xFF3C4B37)
internal val md_theme_dark_onSecondaryContainer = Color(color = 0xFFD7E8CD)
internal val md_theme_dark_tertiary = Color(color = 0xFFA0CFD2)
internal val md_theme_dark_onTertiary = Color(color = 0xFF00373A)
internal val md_theme_dark_tertiaryContainer = Color(color = 0xFF1E4D51)
internal val md_theme_dark_onTertiaryContainer = Color(color = 0xFFBCEBEF)
internal val md_theme_dark_error = Color(color = 0xFFFFB4AB)
internal val md_theme_dark_errorContainer = Color(color = 0xFF93000A)
internal val md_theme_dark_onError = Color(color = 0xFF690005)
internal val md_theme_dark_onErrorContainer = Color(color = 0xFFFFDAD6)
internal val md_theme_dark_background = Color(color = 0xFF1A1C18)
internal val md_theme_dark_onBackground = Color(color = 0xFFE2E3DD)
internal val md_theme_dark_surface = Color(color = 0xFF1A1C18)
internal val md_theme_dark_onSurface = Color(color = 0xFFE2E3DD)
internal val md_theme_dark_surfaceVariant = Color(color = 0xFF43483F)
internal val md_theme_dark_onSurfaceVariant = Color(color = 0xFFC3C8BC)
internal val md_theme_dark_outline = Color(color = 0xFF8D9387)
internal val md_theme_dark_inverseOnSurface = Color(color = 0xFF1A1C18)
internal val md_theme_dark_inverseSurface = Color(color = 0xFFE2E3DD)
internal val md_theme_dark_inversePrimary = Color(color = 0xFF006E08)
internal val md_theme_dark_shadow = Color(color = 0xFF000000)
internal val md_theme_dark_surfaceTint = Color(color = 0xFF7CDC6D)

internal val seed = Color(color = 0xFF006F08)

internal val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
)

internal val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
)
