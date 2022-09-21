package com.vlohachov.moviespot.ui.components.section

import androidx.compose.ui.test.junit4.createComposeRule
import com.google.common.truth.Truth
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography
import org.junit.Rule
import org.junit.Test

class SectionTextStylesTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun defaultSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = DefaultSectionTextStyles(
                    titleTextStyle = Typography.bodyMedium,
                    contentTextStyle = Typography.labelMedium,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.bodyMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.labelMedium)
            }
        }
    }

    @Test
    fun largeSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.headlineLarge)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.bodyLarge)
            }
        }
    }

    @Test
    fun largeSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles(
                    titleTextStyle = Typography.bodyLarge,
                    contentTextStyle = Typography.labelLarge,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.bodyLarge)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.labelLarge)
            }
        }
    }

    @Test
    fun mediumSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.headlineMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.bodyMedium)
            }
        }
    }

    @Test
    fun mediumSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles(
                    titleTextStyle = Typography.bodyMedium,
                    contentTextStyle = Typography.labelMedium,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.bodyMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.labelMedium)
            }
        }
    }

    @Test
    fun smallSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.headlineSmall)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.bodySmall)
            }
        }
    }

    @Test
    fun smallSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles(
                    titleTextStyle = Typography.bodySmall,
                    contentTextStyle = Typography.labelSmall,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(Typography.bodySmall)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(Typography.labelSmall)
            }
        }
    }
}