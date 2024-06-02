package com.vlohachov.shared.presentation.ui.component.button

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ScrollToTopTest {

    @Test
    @JsName(name = "scroll_to_top_displayed")
    fun `scroll to top displayed`() = runComposeUiTest {
        scrollToTop(visible = true)
        verifyScrollToTop(visible = true)
    }

    @Test
    @JsName(name = "scroll_to_top_not_displayed")
    fun `scroll to top not displayed`() = runComposeUiTest {
        scrollToTop(visible = false)
        verifyScrollToTop(visible = false)
    }

    private fun ComposeUiTest.scrollToTop(visible: Boolean) {
        setContent {
            MoviesPotTheme {
                ScrollToTop(visible = visible, gridState = rememberLazyGridState())
            }
        }
    }

    private fun ComposeUiTest.verifyScrollToTop(visible: Boolean) {
        with(onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)) {
            if (visible) {
                assertExists(errorMessageOnFail = "No ScrollToTop component found.")
                assertIsDisplayed()
                assertHasClickAction()
            } else {
                assertDoesNotExist()
            }
        }
    }

}
