package com.vlohachov.moviespot.ui.components.section

import androidx.compose.material3.LocalContentColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.common.truth.Truth
import com.vlohachov.shared.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class SectionColorsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun defaultSectionColorsTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val colors = DefaultSectionColors(
                    titleColor = Color.Red,
                    contentColor = Color.Red,
                )

                Truth.assertThat(colors.titleColor().value)
                    .isEqualTo(Color.Red)
                Truth.assertThat(colors.contentColor().value)
                    .isEqualTo(Color.Red)
            }
        }
    }

    @Test
    fun sectionColorsTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val colors = SectionDefaults.sectionColors()

                Truth.assertThat(colors.titleColor().value)
                    .isEqualTo(LocalContentColor.current)
                Truth.assertThat(colors.contentColor().value)
                    .isEqualTo(LocalContentColor.current)
            }
        }
    }

    @Test
    fun sectionColorsCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val colors = SectionDefaults.sectionColors(
                    titleColor = Color.Red,
                    contentColor = Color.Green,
                )

                Truth.assertThat(colors.titleColor().value)
                    .isEqualTo(Color.Red)
                Truth.assertThat(colors.contentColor().value)
                    .isEqualTo(Color.Green)
            }
        }
    }
}
