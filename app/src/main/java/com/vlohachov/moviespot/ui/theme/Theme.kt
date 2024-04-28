package com.vlohachov.moviespot.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import com.vlohachov.shared.ui.theme.MoviesPotThemeDefaults

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> MoviesPotThemeDefaults.colorScheme
    }

    MoviesPotTheme(colorScheme = colorScheme, content = content)

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            with((view.context as Activity).window) {
                setDecorFitsSystemWindows(false)
                statusBarColor = Color.Transparent.toArgb()
                navigationBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(this, view)
                    .isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(this, view)
                    .isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}
