package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class MoreButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun moreButtonTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                MoreButton(onClick = {})
            }
        }

        verifyMoreButton()
    }

    @Test
    fun customizedMoreButtonTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                MoreButton(
                    modifier = Modifier.size(size = 48.dp),
                    onClick = {},
                    tint = Color.Red,
                )
            }
        }

        verifyMoreButton()
    }

    private fun ComposeTestRule.verifyMoreButton() {
        onNodeWithTag(testTag = MoreButtonDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoreButton component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
        onNodeWithTag(testTag = MoreButtonDefaults.IconTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Icon component found.")
            .assertIsDisplayed()
    }
}