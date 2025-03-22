package com.vlohachov.shared.presentation.ui.screen.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.SettingsRepository
import com.vlohachov.shared.domain.usecase.settings.ApplyDynamicTheme
import com.vlohachov.shared.domain.usecase.settings.LoadSettings
import com.vlohachov.shared.presentation.TestSettings
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository = mock<SettingsRepository> {
        every { getSettings() } returns flow {
            delay(timeMillis = 100)
            emit(value = TestSettings)
        }
    }

    private val viewModel = SettingsViewModel(
        loadSettings = LoadSettings(repository = repository),
        applyDynamicTheme = ApplyDynamicTheme(repository = repository),
    )

    @Test
    @JsName(name = "check_ui_state_loading")
    fun `check ui state loading`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            expect(
                expected = SettingsUiState(isLoading = true),
                message = "Should be uiState with loading = true",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "check_ui_state_settings")
    fun `check ui state settings`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 2)

            expect(
                expected = SettingsUiState(settings = TestSettings),
                message = "Should be uiState with expected setting",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "check_ui_state_error")
    fun `check ui state error`() = runTest {
        val error = IllegalStateException("Error")

        resetAnswers(repository)
        every { repository.getSettings() } returns flow { throw error }

        val viewModel = SettingsViewModel(
            loadSettings = LoadSettings(repository = repository),
            applyDynamicTheme = ApplyDynamicTheme(repository = repository),
        )

        viewModel.uiState.test {
            skipItems(count = 1)

            expect(
                expected = SettingsUiState(error = error),
                message = "Should be error uiState",
            ) { awaitItem() }

            viewModel.resetError()

            expect(
                expected = SettingsUiState(error = null),
                message = "Should be uiState without error",
            ) { awaitItem() }
        }
    }

    @Test
    @JsName(name = "apply_dynamic_theme")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `apply dynamic theme`() = runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        viewModel.applyDynamicTheme(apply = true)

        verifySuspend(mode = atMost(n = 1)) {
            repository.applyDynamicTheme(apply = any())
        }
    }

}
