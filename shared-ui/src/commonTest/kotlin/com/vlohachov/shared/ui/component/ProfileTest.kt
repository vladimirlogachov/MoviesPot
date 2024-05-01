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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalResourceApi::class)
class ProfileTest {

    @Test
    @JsName("non_clickable_profile_displayed_without_error")
    fun `non clickable profile displayed without error`() = runComposeUiTest {
        nonClickableProfile(error = false)
        verifyNonClickableProfile(clickable = false, error = false)
    }

    @Test
    @JsName("non_clickable_profile_displayed_with_error")
    fun `non clickable profile displayed with error`() = runComposeUiTest {
        nonClickableProfile(error = true)
        verifyNonClickableProfile(clickable = false, error = true)
    }

    @Test
    @JsName("clickable_profile_displayed_without_error")
    fun `clickable profile displayed without error`() = runComposeUiTest {
        clickableProfile(error = false)
        verifyNonClickableProfile(clickable = true, error = false)
    }

    @Test
    @JsName("clickable_profile_displayed_with_error")
    fun `clickable profile displayed with error`() = runComposeUiTest {
        clickableProfile(error = true)
        verifyNonClickableProfile(clickable = true, error = true)
    }

    private fun ComposeUiTest.clickableProfile(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Profile(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        title = "Title",
                        body = "Body",
                        onClick = {},
                        error = true,
                    )
                } else {
                    Profile(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        title = "Title",
                        body = "Body",
                        onClick = {},
                    )
                }
            }
        }
    }

    private fun ComposeUiTest.nonClickableProfile(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Profile(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        title = "Title",
                        body = "Body",
                        error = true,
                    )
                } else {
                    Profile(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        title = "Title",
                        body = "Body",
                    )
                }
            }
        }
    }

    private fun ComposeUiTest.verifyNonClickableProfile(clickable: Boolean, error: Boolean) {
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
