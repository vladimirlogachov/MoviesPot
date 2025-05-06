package com.vlohachov.shared.presentation.ui.component.bar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ErrorBarTest {

    @Test
    @JsName(name = "error_bar_displayed")
    fun `error bar displayed`() = runComposeUiTest {
        errorBar(error = IllegalStateException("error"), duration = SnackbarDuration.Indefinite)
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "error_bar_not_displayed")
    fun `error bar not displayed`() = runComposeUiTest {
        errorBar(error = null, duration = SnackbarDuration.Indefinite)
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
    }

    @Test
    @JsName(name = "error_bar_dismissed")
    fun `error bar dismissed`() = runComposeUiTest {
        var dismissed = false
        errorBar(
            error = IllegalStateException("error"),
            duration = SnackbarDuration.Short,
            onDismissed = { dismissed = true }
        )
        mainClock.advanceTimeBy(milliseconds = 4000L) // Equals to SnackbarDuration.Short
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        assertTrue(actual = dismissed)
    }

    private fun ComposeUiTest.errorBar(
        error: Throwable?,
        duration: SnackbarDuration,
        onDismissed: () -> Unit = {},
    ) {
        setContent {
            MoviesPotTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            modifier = Modifier.testTag(tag = ErrorBarDefaults.ErrorTestTag),
                            hostState = snackbarHostState
                        )
                    },
                    content = {
                        ErrorBar(
                            error = error,
                            snackbarHostState = snackbarHostState,
                            duration = duration,
                            onDismissed = onDismissed
                        )
                    }
                )
            }
        }
    }

}
