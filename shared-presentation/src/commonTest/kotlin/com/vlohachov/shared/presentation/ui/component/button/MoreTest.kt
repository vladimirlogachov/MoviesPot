package com.vlohachov.shared.presentation.ui.component.button

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MoreTest {

    @Test
    @JsName(name = "more_displayed")
    fun `more displayed`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                More(onClick = {})
            }
        }

        onNodeWithTag(testTag = MoreDefaults.ButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoreButton component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
        onNodeWithTag(testTag = MoreDefaults.IconTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Icon component found.")
            .assertIsDisplayed()
    }

}
