package com.vlohachov.shared.presentation.ui.component.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Stable
public interface SectionColors {

    @Composable
    public fun titleColor(): State<Color>

    @Composable
    public fun contentColor(): State<Color>

}

public class DefaultSectionColors(
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
