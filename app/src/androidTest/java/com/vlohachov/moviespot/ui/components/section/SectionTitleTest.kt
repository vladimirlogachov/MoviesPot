package com.vlohachov.moviespot.ui.components.section

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class SectionTitleTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun sectionTitleNoIconTest(): Unit = with(composeRule) {
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
    fun sectionTitleIconTest(): Unit = with(composeRule) {
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