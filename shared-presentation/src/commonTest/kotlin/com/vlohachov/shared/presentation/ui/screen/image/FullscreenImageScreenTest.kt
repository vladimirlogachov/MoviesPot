package com.vlohachov.shared.presentation.ui.screen.image

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.runBlocking
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.image_loading_failed
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FullscreenImageScreenTest {

    @Test
    @JsName(name = "check_back_button")
    fun `check back button`() = runComposeUiTest {
        val onBack = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onBack = onBack)
        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onBack() }
    }

    @Test
    @JsName(name = "check_image_preview")
    fun `check image preview`() = runComposeUiTest {
        testContent()
        onNodeWithContentDescription(label = IMAGE_LINK)
            .assertExists(errorMessageOnFail = "No Image component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_broken_image")
    fun `check broken image`() = runComposeUiTest {
        testContent(path = "")
        onNodeWithContentDescription(
            label = runBlocking { getString(resource = Res.string.image_loading_failed) }
        ).assertExists(errorMessageOnFail = "No Image error component found.")
            .assertIsDisplayed()
    }

    private fun ComposeUiTest.testContent(
        path: String = IMAGE_LINK,
        onBack: () -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            FullscreenImage(path = path, onBack = onBack)
        }
    }

    companion object {
        private const val IMAGE_LINK =
            "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
    }

}
