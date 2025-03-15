package com.vlohachov.shared.presentation.ui.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import kotlinx.coroutines.runBlocking
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.error_common_title
import moviespot.shared_presentation.generated.resources.unknown_error_local
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ErrorCardTest {

    @Test
    @JsName(name = "check_error_card_displayed_with_default_values")
    fun `check error card displayed with default values`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                ErrorCard()
            }
        }
        onNodeWithTag(testTag = ErrorCardDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No ErrorCard component found.")
            .assertIsDisplayed()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.error_common_title) })
            .assertExists(errorMessageOnFail = "Title displayed without default value.")
            .assertIsDisplayed()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.unknown_error_local) })
            .assertExists(errorMessageOnFail = "Message displayed without default value.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ErrorCardDefaults.DismissTestTag)
            .assertDoesNotExist()
    }

    @Test
    @JsName(name = "check_error_card_displayed_with_custom_values")
    fun `check error card displayed with custom values`() = runComposeUiTest {
        val onDismiss = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        setContent {
            MoviesPotTheme {
                ErrorCard(
                    title = "Custom title",
                    message = "Custom message",
                    onDismiss = onDismiss,
                )
            }
        }
        onNodeWithTag(testTag = ErrorCardDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No ErrorCard component found.")
            .assertIsDisplayed()
        onNodeWithText(text = "Custom title")
            .assertExists(errorMessageOnFail = "Title displayed without custom value.")
            .assertIsDisplayed()
        onNodeWithText(text = "Custom message")
            .assertExists(errorMessageOnFail = "Message displayed without custom value.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ErrorCardDefaults.DismissTestTag)
            .assertExists(errorMessageOnFail = "No Dismiss component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onDismiss() }
    }

}
