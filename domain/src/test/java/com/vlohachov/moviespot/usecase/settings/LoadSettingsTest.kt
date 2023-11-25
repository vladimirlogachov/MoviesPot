package com.vlohachov.moviespot.usecase.settings

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.SettingsRepository
import com.vlohachov.domain.usecase.settings.LoadSettings
import com.vlohachov.moviespot.data.TestSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoadSettingsTest {

    private val repository = mockk<SettingsRepository>()

    private val useCase = LoadSettings(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every { repository.getSettings() } returns flowOf(TestSettings)

        useCase(param = Unit).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Value`() = runTest {
        every { repository.getSettings() } returns flowOf(TestSettings)

        useCase(param = Unit).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestSettings)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        every { repository.getSettings() } returns flow { throw NullPointerException() }

        useCase(param = Unit).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
