package com.vlohachov.shared.presentation.ui.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SectionTest {

    @Test
    @JsName(name = "default_section")
    fun `default section`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                Section(title = { Text(text = "Text") }) {
                    Text(text = "Content")
                }
            }
        }

        verifySection()
    }

    @Test
    @JsName(name = "customized_section")
    fun `customized section`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                Section(
                    modifier = Modifier.padding(all = 16.dp),
                    title = { Text(text = "Text") },
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    textStyles = SectionDefaults.smallTextStyles(),
                    colors = DefaultSectionColors(
                        titleColor = Color.Red,
                        contentColor = Color.Red,
                    ),
                ) {
                    Text(text = "Content")
                }
            }
        }

        verifySection()
    }

    @Test
    @JsName(name = "default_section_colors")
    fun `default section colors`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val colors = SectionDefaults.sectionColors()

                assertEquals(
                    expected = LocalContentColor.current,
                    actual = colors.titleColor().value,
                )
                assertEquals(
                    expected = LocalContentColor.current,
                    actual = colors.contentColor().value
                )
            }
        }
    }

    @Test
    @JsName(name = "customized_section_colors")
    fun `customized section colors`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val colors = SectionDefaults.sectionColors(
                    titleColor = Color.Red,
                    contentColor = Color.Green,
                )

                assertEquals(expected = Color.Red, actual = colors.titleColor().value)
                assertEquals(expected = Color.Green, actual = colors.contentColor().value)
            }
        }
    }

    @Test
    @JsName(name = "section_default_large_text_styles")
    fun `section default large text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles()

                assertEquals(
                    expected = MaterialTheme.typography.headlineLarge,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.bodyLarge,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    @Test
    @JsName(name = "section_customized_large_text_styles")
    fun `section customized large text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.largeTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodyLarge,
                    contentTextStyle = MaterialTheme.typography.labelLarge,
                )

                assertEquals(
                    expected = MaterialTheme.typography.bodyLarge,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.labelLarge,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    @Test
    @JsName(name = "section_default_medium_text_styles")
    fun `section default medium text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles()

                assertEquals(
                    expected = MaterialTheme.typography.headlineMedium,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.bodyMedium,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    @Test
    @JsName(name = "section_customized_medium_text_styles")
    fun `section customized medium text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.mediumTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodyMedium,
                    contentTextStyle = MaterialTheme.typography.labelMedium,
                )

                assertEquals(
                    expected = MaterialTheme.typography.bodyMedium,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.labelMedium,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    @Test
    @JsName(name = "section_default_small_text_styles")
    fun `section default small text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles()

                assertEquals(
                    expected = MaterialTheme.typography.headlineSmall,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.bodySmall,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    @Test
    @JsName(name = "section_customized_small_text_styles")
    fun `section customized small text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = SectionDefaults.smallTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodySmall,
                    contentTextStyle = MaterialTheme.typography.labelSmall,
                )

                assertEquals(
                    expected = MaterialTheme.typography.bodySmall,
                    actual = textStyles.titleTextStyle().value
                )
                assertEquals(
                    expected = MaterialTheme.typography.labelSmall,
                    actual = textStyles.contentTextStyle().value
                )
            }
        }
    }

    private fun ComposeUiTest.verifySection() {
        onNodeWithTag(testTag = SectionDefaults.SectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Section component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionDefaults.SectionTitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child title component found")
            .assertIsDisplayed()
            .onChild()
            .assertTextEquals("Text")
        onNodeWithTag(testTag = SectionDefaults.SectionContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child content component found")
            .assertIsDisplayed()
            .onChild()
            .assertTextEquals("Content")
    }

}
