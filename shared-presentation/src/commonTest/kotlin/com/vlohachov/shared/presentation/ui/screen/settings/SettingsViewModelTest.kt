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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.expect

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testScope = TestScope()

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

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(
            dispatcher = StandardTestDispatcher(scheduler = testScope.testScheduler)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @JsName(name = "check_ui_state_loading")
    fun `check ui state loading`() = testScope.runTest {
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
    fun `check ui state settings`() = testScope.runTest {
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
    fun `check ui state error`() = testScope.runTest {
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
    fun `apply dynamic theme`() = testScope.runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        viewModel.applyDynamicTheme(apply = true)

        advanceUntilIdle()

        verifySuspend(mode = atMost(n = 1)) {
            repository.applyDynamicTheme(apply = any())
        }
    }

}
