package com.vlohachov.shared.domain

import app.cash.turbine.test
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs

class ResultTest {

    private companion object {
        const val TEST_VALUE = "value"
        val TestException = Exception("Illegal $TEST_VALUE")
    }

    private val testFlow = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        testFlow.asResult().test {
            assertIs<Result.Loading>(value = awaitItem())
        }
    }

    @Test
    @JsName("result_flow_emits_Success")
    fun `result flow emits Success`() = runTest {
        testFlow.asResult().test {
            testFlow.emit(value = TEST_VALUE)
            assertIs<Result.Success<String>>(value = expectMostRecentItem())
        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        testFlow.onEach { throw TestException }
            .asResult()
            .test {
                testFlow.emit(value = TEST_VALUE)
                assertIs<Result.Error>(value = expectMostRecentItem())
            }
    }

}
