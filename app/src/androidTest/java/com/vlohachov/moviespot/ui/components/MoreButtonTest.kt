package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.ui.components.button.More
import com.vlohachov.moviespot.ui.components.button.MoreButtonPreview
import com.vlohachov.moviespot.ui.components.button.MoreDefaults
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
                More(onClick = {})
            }
        }

        verifyMoreButton()
    }

    @Test
    fun customizedMoreButtonTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                More(
                    modifier = Modifier.size(size = 48.dp),
                    onClick = {},
                    tint = Color.Red,
                )
            }
        }

        verifyMoreButton()
    }

    @Test
    fun previewTest(): Unit = with(composeRule) {
        setContent {
            MoreButtonPreview()
        }

        onAllNodes(hasTestTag(testTag = MoreDefaults.ButtonTestTag))
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assertHasClickAction()
    }

    private fun ComposeTestRule.verifyMoreButton() {
        onNodeWithTag(testTag = MoreDefaults.ButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoreButton component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
        onNodeWithTag(testTag = MoreDefaults.IconTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Icon component found.")
            .assertIsDisplayed()
    }
}
