package com.vlohachov.shared.ui.component.section

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SectionColorsTest {

    @Test
    @JsName("default_section_colors")
    fun `default section colors`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val colors = DefaultSectionColors(
                    titleColor = Color.Red,
                    contentColor = Color.Red,
                )

                assertEquals(expected = Color.Red, actual = colors.titleColor().value)
                assertEquals(expected = Color.Red, actual = colors.contentColor().value)
            }
        }
    }

}
