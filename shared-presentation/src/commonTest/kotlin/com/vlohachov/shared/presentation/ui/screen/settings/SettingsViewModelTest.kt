package com.vlohachov.shared.presentation.ui.screen.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.SettingsRepository
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.TestSettings
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

class SettingsViewModelTest {

    private val repository = mock<SettingsRepository> {
        every { getSettings() } returns flow {
            delay(timeMillis = 50)
            emit(value = TestSettings)
        }
    }

    private val viewModel = SettingsViewModel(
        loadSettings = LoadSettings(repository = repository),
        applyDynamicTheme = ApplyDynamicTheme(repository = repository),
    )

    @Test
    @JsName(name = "check if ui state loading")
    fun `check if ui state loading`() = runTest {
        viewModel.uiState.test {
            expect(
                expected = SettingsUiState(isLoading = true),
                message = "Should be uiState with loading = true",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "check ui state settings")
    fun `check ui state settings`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)
            expect(
                expected = SettingsUiState(settings = TestSettings),
                message = "Should be uiState with expected setting",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "check_if_ui_state_error")
    fun `check if ui state error`() = runTest {
        val error = IllegalStateException("Error")

        resetAnswers(repository)
        every { repository.getSettings() } returns flow { throw error }

        SettingsViewModel(
            loadSettings = LoadSettings(repository = repository),
            applyDynamicTheme = ApplyDynamicTheme(repository = repository),
        ).uiState.test {
            expect(
                expected = SettingsUiState(error = error),
                message = "Should be error uiState",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "apply_dynamic_theme_success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `apply dynamic theme success`() = runTest {
        val param = ApplyDynamicTheme.Param(apply = true)

        viewModel.applyDynamicTheme(apply = param.apply)

        advanceUntilIdle()

        verifySuspend(mode = atMost(n = 1)) {
            repository.applyDynamicTheme(apply = param.apply)
        }
    }

}
