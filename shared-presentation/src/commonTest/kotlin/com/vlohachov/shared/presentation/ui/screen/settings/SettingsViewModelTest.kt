package com.vlohachov.shared.presentation.ui.screen.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.SettingsRepository
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.TestSettings
import com.vlohachov.shared.presentation.runViewModelTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository = mock<SettingsRepository>()

    @Test
    @JsName(name = "ui_state_is_loaded_successfully")
    fun `ui state is loaded successfully`() = runViewModelTest {
        every { repository.getSettings() } returns flow {
            delay(timeMillis = 100) // simulate loading
            emit(value = TestSettings)
        }

        val viewModel = createViewModel()

        viewModel.uiState.test {
            expect(expected = SettingsUiState()) { awaitItem() }
            expect(expected = SettingsUiState(isLoading = true)) { awaitItem() }
            expect(expected = SettingsUiState(isLoading = false, settings = TestSettings)) {
                awaitItem()
            }
        }
    }

    @Test
    @JsName(name = "ui_state_is_error")
    fun `ui state is error`() = runViewModelTest {
        val error = IllegalStateException("Error")
        every { repository.getSettings() } returns flow {
            delay(timeMillis = 100) // simulate loading
            throw error
        }

        val viewModel = createViewModel()

        viewModel.uiState.test {
            expect(expected = SettingsUiState()) { awaitItem() }
            expect(expected = SettingsUiState(isLoading = true)) { awaitItem() }
            expect(expected = SettingsUiState(isLoading = false, error = error)) { awaitItem() }

            viewModel.resetError()

            expect(expected = SettingsUiState(isLoading = false, error = null)) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "apply_dynamic_theme_is_called")
    fun `apply dynamic theme is called`() = runViewModelTest {
        every { repository.getSettings() } returns flowOf()
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        val viewModel = createViewModel()

        viewModel.applyDynamicTheme(apply = true)
        
        advanceUntilIdle()

        verifySuspend(mode = exactly(n = 1)) {
            repository.applyDynamicTheme(apply = true)
        }
    }

    private fun createViewModel() = SettingsViewModel(
        loadSettings = LoadSettings(repository = repository),
        applyDynamicTheme = ApplyDynamicTheme(repository = repository),
    )

}
