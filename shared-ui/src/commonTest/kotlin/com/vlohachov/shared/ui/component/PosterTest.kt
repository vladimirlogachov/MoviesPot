package com.vlohachov.shared.ui.component

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.ic_launcher_foreground
import org.jetbrains.compose.resources.painterResource
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PosterTest {


    @Test
    @JsName(name = "non_clickable_poster_displayed_without_error")
    fun `non clickable poster displayed without error`() = runComposeUiTest {
        nonClickablePoster(error = false)
        verifyNonClickablePoster(clickable = false, error = false)
    }

    @Test
    @JsName(name = "non_clickable_poster_displayed_with_error")
    fun `non clickable poster displayed with error`() = runComposeUiTest {
        nonClickablePoster(error = true)
        verifyNonClickablePoster(clickable = false, error = true)
    }

    @Test
    @JsName(name = "clickable_poster_displayed_without_error")
    fun `clickable poster displayed without error`() = runComposeUiTest {
        clickablePoster(error = false)
        verifyNonClickablePoster(clickable = true, error = false)
    }

    @Test
    @JsName(name = "clickable_poster_displayed_with_error")
    fun `clickable poster displayed with error`() = runComposeUiTest {
        clickablePoster(error = true)
        verifyNonClickablePoster(clickable = true, error = true)
    }

    private fun ComposeUiTest.clickablePoster(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Poster(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        onClick = {},
                        error = true,
                    )
                } else {
                    Poster(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        onClick = {},
                    )
                }
            }
        }
    }

    private fun ComposeUiTest.nonClickablePoster(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Poster(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        error = true,
                    )
                } else {
                    Poster(painter = painterResource(resource = Res.drawable.ic_launcher_foreground))
                }
            }
        }
    }

    private fun ComposeUiTest.verifyNonClickablePoster(clickable: Boolean, error: Boolean) {
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
