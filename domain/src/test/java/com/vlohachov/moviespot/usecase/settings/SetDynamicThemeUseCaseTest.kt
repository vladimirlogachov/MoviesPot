package com.vlohachov.moviespot.usecase.settings

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.SettingsRepository
import com.vlohachov.domain.usecase.settings.SetDynamicThemeUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetDynamicThemeUseCaseTest {

    private companion object {
        val TestParam = SetDynamicThemeUseCase.Param(dynamicTheme = true)
    }

    private val repository = mockk<SettingsRepository>()

    private val useCase = SetDynamicThemeUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        coJustRun { repository.applyDynamicTheme(apply = any()) }

        useCase.resultFlow(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `Result flow emits Value`() = runTest {
        coJustRun { repository.applyDynamicTheme(apply = any()) }

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestParam.dynamicTheme)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Error`() = runTest {
        coEvery { repository.applyDynamicTheme(apply = any()) } throws NullPointerException()

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }
}
