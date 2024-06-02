package com.vlohachov.shared.presentation.ui.component.bar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class AppBarTest {

    @Test
    @JsName(name = "app_bar_displayed_with_title")
    fun `app bar displayed with title`() = runComposeUiTest {
        appBar(showTitle = true)
        verifyAppBar(showTitle = true)
    }

    @Test
    @JsName(name = "app_bar_displayed_without_title")
    fun `app bar displayed without title`() = runComposeUiTest {
        appBar(showTitle = false)
        verifyAppBar(showTitle = false)
    }

    @Test
    @JsName(name = "large_app_bar_displayed_with_title")
    fun `large app bar displayed with title`() = runComposeUiTest {
        largeAppBar(showTitle = true)
        verifyAppBar(showTitle = true)
    }

    @Test
    @JsName(name = "large_app_bar_displayed_without_title")
    fun `large app bar displayed without title`() = runComposeUiTest {
        largeAppBar(showTitle = false)
        verifyAppBar(showTitle = false)
    }

    private fun ComposeUiTest.appBar(showTitle: Boolean) {
        setContent {
            MoviesPotTheme {
                AppBar(
                    modifier = Modifier,
                    title = "Title",
                    onBackClick = {},
                    showTitle = showTitle,
                )
            }
        }
    }

    private fun ComposeUiTest.largeAppBar(showTitle: Boolean) {
        setContent {
            MoviesPotTheme {
                LargeAppBar(
                    modifier = Modifier,
                    title = "Title",
                    onBackClick = {},
                    showTitle = showTitle,
                )
            }
        }
    }

    private fun ComposeUiTest.verifyAppBar(showTitle: Boolean) {
        onNodeWithTag(testTag = AppBarDefaults.AppBarTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No AppBar component found.")
            .assertIsDisplayed()
        with(onNodeWithTag(testTag = AppBarDefaults.TitleTestTag, useUnmergedTree = false)) {
            if (showTitle) {
                assertExists(errorMessageOnFail = "No child Text component found.")
                assertIsDisplayed()
            } else {
                assertDoesNotExist()
            }
        }
        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

}
