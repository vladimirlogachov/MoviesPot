package com.vlohachov.shared.presentation.ui.component.section

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SectionTitleTest {

    @Test
    @JsName(name = "section_title_without_icon")
    fun `section title without icon`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                SectionTitle(text = "Title")
            }
        }

        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTextTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text title component found.")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        onNodeWithTag(testTag = SectionTitleDefaults.TitleIconTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    @JsName(name = "section_title_with_icon")
    fun `section title with icon`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                SectionTitle(
                    text = "Title",
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                        )
                    },
                )
            }
        }

        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTextTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text title component found.")
            .assertIsDisplayed()
            .assertTextEquals("Title")
        onNodeWithTag(testTag = SectionTitleDefaults.TitleIconTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Icon trailing component found.")
            .assertIsDisplayed()
    }

}
