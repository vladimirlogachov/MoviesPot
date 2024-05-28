package com.vlohachov.shared.ui.screen.settings

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.TestSettings
import com.vlohachov.shared.domain.repository.SettingsRepository
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.ui.BuildConfig
import com.vlohachov.shared.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.app_version
import moviespot.shared_ui.generated.resources.author
import moviespot.shared_ui.generated.resources.author_name
import moviespot.shared_ui.generated.resources.settings
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    private val repository = mock<SettingsRepository> {
        every { getSettings() } returns emptyFlow()
    }

    private val loadSettings = LoadSettings(repository = repository)
    private val applyDynamicTheme = ApplyDynamicTheme(repository = repository)

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
    @JsName(name = "check_loading_state")
    fun `check loading state`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_success_state")
    fun `check success state`() = runComposeUiTest {
        every { repository.getSettings() } returns flowOf(value = TestSettings)
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeTestTag)
            .assertExists(errorMessageOnFail = "No DynamicTheme component found")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 2)
    }

    @Test
    @JsName(name = "check_error_state")
    fun `check error state`() = runComposeUiTest {
        every { repository.getSettings() } returns flow { error(message = "Error") }
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_apply_dynamic_theme")
    fun `check apply dynamic theme`() = runComposeUiTest {
        every { repository.getSettings() } returns flowOf(value = TestSettings)
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeToggleTestTag)
            .assertExists(errorMessageOnFail = "No Toggle component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()
        verifySuspend(mode = atMost(n = 1)) { repository.applyDynamicTheme(apply = any()) }
    }

    @Test
    @JsName(name = "check_app_version")
    fun `check app version`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.AppVersionTestTag)
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                runBlocking {
                    getString(resource = Res.string.app_version)
                } + BuildConfig.VERSION_NAME
            )
    }

    @Test
    @JsName(name = "check_author")
    fun `check author`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = SettingsDefaults.AppAuthorTestTag)
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                runBlocking {
                    getString(resource = Res.string.author) + getString(resource = Res.string.author_name)
                }
            )
    }

    private fun ComposeUiTest.testContent(onBack: () -> Unit = {}) = setContent {
        MoviesPotTheme {
            Settings(
                onBack = onBack,
                viewModel = SettingsViewModel(
                    loadSettings = loadSettings,
                    applyDynamicTheme = applyDynamicTheme,
                ),
                isDynamicThemeAvailable = true,
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
