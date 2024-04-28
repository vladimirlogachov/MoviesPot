package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class ProfileTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nonClickableProfileTest() {
        composeRule.nonClickableProfile(error = false)
        composeRule.verifyNonClickableProfile(clickable = false, error = false)
    }

    @Test
    fun nonClickableProfileErrorTest() {
        composeRule.nonClickableProfile(error = true)
        composeRule.verifyNonClickableProfile(clickable = false, error = true)
    }

    @Test
    fun clickableProfileTest() {
        composeRule.clickableProfile(error = false)
        composeRule.verifyNonClickableProfile(clickable = true, error = false)
    }

    @Test
    fun clickableProfileErrorTest() {
        composeRule.clickableProfile(error = true)
        composeRule.verifyNonClickableProfile(clickable = true, error = true)
    }

    @Test
    fun previewTest() {
        composeRule.setContent {
            ProfilePreview()
        }

        composeRule.onAllNodes(hasTestTag(testTag = ProfileDefaults.ProfileTestTag))
            .assertCountEquals(expectedSize = 2)
            .onLast()
            .assertHasClickAction()
    }

    private fun ComposeContentTestRule.clickableProfile(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Profile(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        title = "Title",
                        body = "Body",
                        onClick = {},
                        error = true,
                        footerPadding = ProfileDefaults.FooterPadding,
                        shape = ProfileDefaults.Shape,
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        tonalElevation = ProfileDefaults.TonalElevation,
                        shadowElevation = ProfileDefaults.ShadowElevation,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                    )
                } else {
                    Profile(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        title = "Title",
                        body = "Body",
                        onClick = {},
                    )
                }
            }
        }
    }

    private fun ComposeContentTestRule.nonClickableProfile(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Profile(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        title = "Title",
                        body = "Body",
                        error = true,
                        footerPadding = ProfileDefaults.FooterPadding,
                        shape = ProfileDefaults.Shape,
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        tonalElevation = ProfileDefaults.TonalElevation,
                        shadowElevation = ProfileDefaults.ShadowElevation,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                    )
                } else {
                    Profile(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        title = "Title",
                        body = "Body",
                    )
                }
            }
        }
    }

    private fun ComposeTestRule.verifyNonClickableProfile(clickable: Boolean, error: Boolean) {
        with(onNodeWithTag(testTag = ProfileDefaults.ProfileTestTag, useUnmergedTree = true)) {
            assertExists(errorMessageOnFail = "No Profile component found.")
            assertIsDisplayed()
            if (clickable) {
                assertHasClickAction()
            } else {
                assertHasNoClickAction()
            }
        }
        onNodeWithTag(testTag = ProfileDefaults.ImageTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Image component found.")
            .assertIsDisplayed()
        with(onNodeWithTag(testTag = ProfileDefaults.ErrorTestTag, useUnmergedTree = true)) {
            if (error) {
                assertExists(errorMessageOnFail = "No child Icon error component found.")
                assertIsDisplayed()
            } else {
                assertDoesNotExist()
            }
        }
        onNodeWithTag(testTag = ProfileDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text title component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ProfileDefaults.BodyTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text body component found.")
            .assertIsDisplayed()
    }
}
