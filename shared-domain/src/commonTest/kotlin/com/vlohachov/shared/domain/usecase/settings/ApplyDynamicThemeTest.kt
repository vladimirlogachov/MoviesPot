package com.vlohachov.shared.domain.usecase.settings

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ApplyDynamicThemeTest {

    private companion object {
        val TestParam = ApplyDynamicTheme.Param(apply = true)
    }

    private val repository = mockk<SettingsRepository>()

    private val useCase = ApplyDynamicTheme(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        coJustRun { repository.applyDynamicTheme(apply = any()) }

        useCase(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Value`() = runTest {
        coJustRun { repository.applyDynamicTheme(apply = any()) }

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestParam.apply)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        coEvery { repository.applyDynamicTheme(apply = any()) } throws NullPointerException()

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
