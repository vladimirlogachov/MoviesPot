package com.vlohachov.shared.presentation.ui.component.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.text.TextStyle

@Stable
public interface SectionTextStyles {

    @Composable
    public fun titleTextStyle(): State<TextStyle>

    @Composable
    public fun contentTextStyle(): State<TextStyle>

}

public class DefaultSectionTextStyles(
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
