package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class PosterTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nonClickablePosterTest() {
        composeRule.nonClickablePoster(error = false)
        composeRule.verifyNonClickablePoster(clickable = false, error = false)
    }

    @Test
    fun nonClickablePosterErrorTest() {
        composeRule.nonClickablePoster(error = true)
        composeRule.verifyNonClickablePoster(clickable = false, error = true)
    }

    @Test
    fun clickablePosterTest() {
        composeRule.clickablePoster(error = false)
        composeRule.verifyNonClickablePoster(clickable = true, error = false)
    }

    @Test
    fun clickablePosterErrorTest() {
        composeRule.clickablePoster(error = true)
        composeRule.verifyNonClickablePoster(clickable = true, error = true)
    }

    @Test
    fun previewTest() {
        composeRule.setContent {
            PosterPreview()
        }

        composeRule.onAllNodes(hasTestTag(testTag = PosterDefaults.PosterTestTag))
            .assertCountEquals(expectedSize = 2)
            .onLast()
            .assertHasClickAction()
    }

    private fun ComposeContentTestRule.clickablePoster(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Poster(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        onClick = {},
                        error = true,
                        shape = PosterDefaults.Shape,
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        tonalElevation = PosterDefaults.TonalElevation,
                        shadowElevation = PosterDefaults.ShadowElevation,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                    )
                } else {
                    Poster(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        onClick = {},
                    )
                }
            }
        }
    }

    private fun ComposeContentTestRule.nonClickablePoster(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Poster(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                        error = true,
                        shape = PosterDefaults.Shape,
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        tonalElevation = PosterDefaults.TonalElevation,
                        shadowElevation = PosterDefaults.ShadowElevation,
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                    )
                } else {
                    Poster(
                        painter = rememberAsyncImagePainter(
                            model = "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
                        ),
                    )
                }
            }
        }
    }

    private fun ComposeTestRule.verifyNonClickablePoster(clickable: Boolean, error: Boolean) {
        with(onNodeWithTag(testTag = PosterDefaults.PosterTestTag, useUnmergedTree = true)) {
            assertExists(errorMessageOnFail = "No Profile component found.")
            assertIsDisplayed()
            if (clickable) {
                assertHasClickAction()
            } else {
                assertHasNoClickAction()
            }
        }
        onNodeWithTag(testTag = PosterDefaults.ImageTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Image component found.")
            .assertIsDisplayed()
        with(onNodeWithTag(testTag = PosterDefaults.ErrorTestTag, useUnmergedTree = true)) {
            if (error) {
                assertExists(errorMessageOnFail = "No child Icon error component found.")
                assertIsDisplayed()
            } else {
                assertDoesNotExist()
            }
        }
    }
}