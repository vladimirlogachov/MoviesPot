package com.vlohachov.moviespot.ui.components.section

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.common.truth.Truth
import com.vlohachov.shared.theme.MoviesPotTheme
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
                    titleTextStyle = MaterialTheme.typography.bodyMedium,
                    contentTextStyle = MaterialTheme.typography.labelMedium,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodyMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.labelMedium)
            }
        }
    }

    @Test
    fun largeSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.headlineLarge)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodyLarge)
            }
        }
    }

    @Test
    fun largeSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodyLarge,
                    contentTextStyle = MaterialTheme.typography.labelLarge,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodyLarge)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.labelLarge)
            }
        }
    }

    @Test
    fun mediumSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.headlineMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodyMedium)
            }
        }
    }

    @Test
    fun mediumSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodyMedium,
                    contentTextStyle = MaterialTheme.typography.labelMedium,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodyMedium)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.labelMedium)
            }
        }
    }

    @Test
    fun smallSectionTextStylesTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles()

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.headlineSmall)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodySmall)
            }
        }
    }

    @Test
    fun smallSectionTextStylesCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodySmall,
                    contentTextStyle = MaterialTheme.typography.labelSmall,
                )

                Truth.assertThat(textStyles.titleTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.bodySmall)
                Truth.assertThat(textStyles.contentTextStyle().value)
                    .isEqualTo(MaterialTheme.typography.labelSmall)
            }
        }
    }
}
