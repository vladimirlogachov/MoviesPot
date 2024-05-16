package com.vlohachov.shared.domain.usecase.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestSettings
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.repository.SettingsRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs

class LoadSettingsTest {

    private val repository = mock<SettingsRepository>()

    private val useCase = LoadSettings(repository = repository)

    @Test
    @JsName(name = "result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every { repository.getSettings() } returns flowOf(TestSettings)

        useCase(param = Unit).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName(name = "result_flow_emits_Value")
    fun `result flow emits Value`() = runTest {
        every { repository.getSettings() } returns flowOf(TestSettings)

        useCase(param = Unit).test {
            assertIs<Result.Success<Settings>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName(name = "result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every { repository.getSettings() } returns flow { throw NullPointerException() }

        useCase(param = Unit).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
