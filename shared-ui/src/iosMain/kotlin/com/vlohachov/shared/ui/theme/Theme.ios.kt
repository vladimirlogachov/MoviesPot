package com.vlohachov.shared.ui.theme

import androidx.compose.runtime.Composable

@Composable
public actual fun MoviesPotTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    MoviesPotTheme(content = content)
}
