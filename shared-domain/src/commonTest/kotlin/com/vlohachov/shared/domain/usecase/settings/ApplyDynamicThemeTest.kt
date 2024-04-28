package com.vlohachov.shared.domain.usecase.settings

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.repository.SettingsRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs

class ApplyDynamicThemeTest {

    private companion object {
        val TestParam = ApplyDynamicTheme.Param(apply = true)
    }

    private val repository = mock<SettingsRepository>()

    private val useCase = ApplyDynamicTheme(repository = repository)

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        useCase(param = TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Value")
    fun `result flow emits Value`() = runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        useCase(param = TestParam).test {
            assertIs<Result.Success<Boolean>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } throws NullPointerException()

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
