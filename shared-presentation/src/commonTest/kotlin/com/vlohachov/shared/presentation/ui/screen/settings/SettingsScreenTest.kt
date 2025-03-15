package com.vlohachov.shared.presentation.ui.screen.settings

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.presentation.BuildConfig
import com.vlohachov.shared.presentation.ui.component.ErrorCardDefaults
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.app_version
import moviespot.shared_presentation.generated.resources.author
import moviespot.shared_presentation.generated.resources.author_name
import moviespot.shared_presentation.generated.resources.settings
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.settings) })
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

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
        verify(mode = atMost(n = 1)) { onBack() }
    }

    @Test
    @JsName(name = "check_progress_visibility")
    fun `check progress visibility`() = runComposeUiTest {
        testContent(uiState = SettingsUiState(isLoading = true))
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_content")
    fun `check content`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeTestTag)
            .assertExists(errorMessageOnFail = "No DynamicTheme component found")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 2)
        onNodeWithTag(testTag = SettingsDefaults.AppVersionTestTag)
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                runBlocking {
                    getString(resource = Res.string.app_version) + BuildConfig.VERSION_NAME
                }
            )
        onNodeWithTag(testTag = SettingsDefaults.AppAuthorTestTag)
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                runBlocking {
                    getString(resource = Res.string.author) + getString(resource = Res.string.author_name)
                }
            )
    }

    @Test
    @JsName(name = "check_error")
    fun `check error`() = runComposeUiTest {
        val onResetError = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(
            uiState = SettingsUiState(error = Exception("error")),
            onResetError = onResetError,
        )
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeToggleTestTag)
            .assertExists(errorMessageOnFail = "No Toggle component found.")
            .assertIsDisplayed()
            .assertIsNotEnabled()
        onNodeWithTag(testTag = ErrorCardDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ErrorCardDefaults.DismissTestTag)
            .assertExists(errorMessageOnFail = "No Dismiss component found")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onResetError() }
    }

    @Test
    @JsName(name = "check_apply_dynamic_theme")
    fun `check apply dynamic theme`() = runComposeUiTest {
        val onApplyDynamicTheme = mock<(Boolean) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        testContent(
            uiState = SettingsUiState(isDynamicThemeAvailable = true),
            onDynamicTheme = onApplyDynamicTheme,
        )
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeToggleTestTag)
            .assertExists(errorMessageOnFail = "No Toggle component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()
        verifySuspend(mode = atMost(n = 1)) { onApplyDynamicTheme(any()) }
    }

    private fun ComposeUiTest.testContent(
        uiState: SettingsUiState = SettingsUiState(),
        onBack: () -> Unit = {},
        onResetError: () -> Unit = {},
        onDynamicTheme: (dynamicTheme: Boolean) -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            Settings(
                onBack = onBack,
                onResetError = onResetError,
                onDynamicTheme = onDynamicTheme,
                uiState = uiState,
            )
        }
    }

}
