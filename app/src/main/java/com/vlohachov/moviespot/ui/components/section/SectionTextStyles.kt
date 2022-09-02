package com.vlohachov.moviespot.ui.components.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.text.TextStyle

@Stable
interface SectionTextStyles {

    @Composable
    fun titleTextStyle(): State<TextStyle>

    @Composable
    fun contentTextStyle(): State<TextStyle>
}

class DefaultSectionTextStyles(
    private val titleTextStyle: TextStyle,
    private val contentTextStyle: TextStyle,
) : SectionTextStyles {

    @Composable
    override fun titleTextStyle(): State<TextStyle> {
        return rememberUpdatedState(newValue = titleTextStyle)
    }

    @Composable
    override fun contentTextStyle(): State<TextStyle> {
        return rememberUpdatedState(newValue = contentTextStyle)
    }
}