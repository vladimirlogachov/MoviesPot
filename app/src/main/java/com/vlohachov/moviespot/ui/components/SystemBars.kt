package com.vlohachov.moviespot.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetSystemBarsColor(
    colorTransitionFraction: Float,
    color: Color = MaterialTheme.colorScheme.surface,
    scrolledColor: Color = MaterialTheme.colorScheme.applyTonalElevation(
        backgroundColor = color,
        elevation = 3.dp,
    ),
    systemUiController: SystemUiController = rememberSystemUiController(),
    useDarkIcons: Boolean = !isSystemInDarkTheme(),
    twoRowsTopAppBar: Boolean = false,
) {
    val fraction = if (twoRowsTopAppBar) {
        colorTransitionFraction
    } else if (colorTransitionFraction > 0.01f) 1f else 0f
    val targetColor = lerp(
        color,
        scrolledColor,
        FastOutLinearInEasing.transform(fraction = fraction),
    )
    val appBarContainerColor by if (twoRowsTopAppBar) {
        rememberUpdatedState(newValue = targetColor)
    } else {
        animateColorAsState(
            targetValue = targetColor,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
    }

    DisposableEffect(systemUiController, useDarkIcons, appBarContainerColor) {
        systemUiController.setSystemBarsColor(
            color = appBarContainerColor,
            darkIcons = useDarkIcons,
        )
        onDispose { }
    }
}

private fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    return if (backgroundColor == surface) {
        surfaceColorAtElevation(elevation)
    } else {
        backgroundColor
    }
}