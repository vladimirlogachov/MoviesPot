package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetSystemBarsColor(
    color: Color,
    systemUiController: SystemUiController = rememberSystemUiController(),
    useDarkIcons: Boolean = !isSystemInDarkTheme(),
) {
    DisposableEffect(systemUiController, useDarkIcons, color) {
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = useDarkIcons,
        )
        onDispose {}
    }
}
