package com.vlohachov.shared.presentation.ui.screen.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.repository.SettingsRepository
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.TestSettings
import com.vlohachov.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SettingsViewModelTest {

    private val repository = mock<SettingsRepository> {
        every { getSettings() } returns flowOf(value = TestSettings)
    }

    private val viewModel = SettingsViewModel(
        loadSettings = LoadSettings(repository = repository),
        applyDynamicTheme = ApplyDynamicTheme(repository = repository),
    )

    @Test
    @JsName(name = "settings_flow_emits_Loading")
    fun `settings flow emits Loading`() = runTest {
        viewModel.viewState.test {
            assertIs<ViewState.Loading>(value = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    @JsName(name = "settings_flow_emits_Success")
    fun `settings flow emits Success`() = runTest {
        viewModel.viewState.test {
            skipItems(count = 1)
            assertIs<ViewState.Success<Settings>>(value = awaitItem())
        }
    }

    @Test
    @JsName(name = "settings_flow_emits_Error")
    fun `settings flow emits Error`() = runTest {
        resetAnswers(repository)
        every { repository.getSettings() } returns flow { error(message = "Error") }

        SettingsViewModel(
            loadSettings = LoadSettings(repository = repository),
            applyDynamicTheme = ApplyDynamicTheme(repository = repository),
        ).viewState.test {
            skipItems(count = 1)
            assertIs<ViewState.Error>(value = awaitItem())
        }
    }

    @Test
    @JsName(name = "apply_dynamic_theme_success")
    fun `apply dynamic theme success`() = runTest {
        val param = ApplyDynamicTheme.Param(apply = true)

        viewModel.applyDynamicTheme(apply = param.apply)

        verifySuspend(mode = atMost(n = 2)) { repository.applyDynamicTheme(apply = any()) }
    }

    @Test
    @JsName(name = "error_is_set_and_consumed")
    fun `error is set and consumed`() = runTest {
        viewModel.error.test {
            assertNull(actual = awaitItem())
            viewModel.onError(error = Exception())
            assertNotNull(actual = awaitItem())
            viewModel.onErrorConsumed()
            assertNull(actual = awaitItem())
        }
    }

}
