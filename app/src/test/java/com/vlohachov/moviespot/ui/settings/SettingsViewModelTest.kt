package com.vlohachov.moviespot.ui.settings

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.usecase.settings.ApplyDynamicThemeUseCase
import com.vlohachov.domain.usecase.settings.GetSettingsUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestSettings
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule(dispatcher = UnconfinedTestDispatcher())

    private val getSettings = mockk<GetSettingsUseCase>()
    private val applyDynamicTheme = mockk<ApplyDynamicThemeUseCase>()

    private val settingsFlow = MutableStateFlow<Result<Settings>>(value = Result.Loading)

    private val viewModel by lazy {
        every { getSettings.resultFlow(param = Unit) } returns settingsFlow

        SettingsViewModel(
            getSettings = getSettings,
            applyDynamicTheme = applyDynamicTheme,
        )
    }

    @Test
    fun `Settings Loading`() = runTest {
        viewModel.viewState.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isEqualTo(ViewState.Loading)
        }
    }

    @Test
    fun `Settings Success`() = runTest {
        viewModel.viewState.test {
            skipItems(count = 1)

            settingsFlow.tryEmit(value = Result.Success(value = TestSettings))

            val actual = awaitItem()

            Truth.assertThat(actual).isEqualTo(ViewState.Success(data = TestSettings))
        }
    }

    @Test
    fun `Settings Error`() = runTest {
        val error = NullPointerException()

        viewModel.viewState.test {
            skipItems(count = 1)

            settingsFlow.tryEmit(value = Result.Error(exception = error))

            val actual = awaitItem()

            Truth.assertThat(actual).isEqualTo(ViewState.Error(error = error))
        }
    }

    @Test
    fun `apply dynamic theme`() = runTest {
        val param = ApplyDynamicThemeUseCase.Param(apply = true)

        every {
            applyDynamicTheme.resultFlow(param = param)
        } returns flowOf(value = Result.Success(value = param.apply))

        viewModel.applyDynamicTheme(apply = param.apply)

        verify(exactly = 1) { applyDynamicTheme.resultFlow(param = any()) }
    }

    @Test
    fun `error is set and consumed`() = runTest {
        viewModel.onError(error = Exception())

        Truth.assertThat(viewModel.error).isNotNull()

        viewModel.onErrorConsumed()

        Truth.assertThat(viewModel.error).isNull()
    }
}
