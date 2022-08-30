package com.vlohachov.moviespot.ui.components.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Stable
interface SectionColors {

    @Composable
    fun titleColor(): State<Color>

    @Composable
    fun contentColor(): State<Color>
}

class DefaultSectionColors(
    private val titleColor: Color,
    private val contentColor: Color,
) : SectionColors {

    @Composable
    override fun titleColor(): State<Color> {
        return rememberUpdatedState(newValue = titleColor)
    }

    @Composable
    override fun contentColor(): State<Color> {
        return rememberUpdatedState(newValue = contentColor)
    }

}