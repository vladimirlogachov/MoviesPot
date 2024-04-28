package com.vlohachov.shared.domain

import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ResultTest {

    private companion object {
        const val TestValue = "value"
        val TestException = Exception("Illegal $TestValue")
    }

    private val testFlow = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    @Test
    fun `result flow emits Loading`() = runTest {
        testFlow.asResult().test {
            testFlow.emit(value = TestValue)

            val expected = Result.Loading
            val actual = awaitItem()

            skipItems(count = 1)

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Success`() = runTest {
        testFlow.asResult().test {
            testFlow.emit(value = TestValue)

            skipItems(count = 1)

            val expected = Result.Success(value = TestValue)
            val actual = awaitItem()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        testFlow
            .onEach { throw TestException }
            .asResult()
            .test {
                testFlow.emit(value = TestValue)

                skipItems(count = 1)

                val expected = Result.Error(exception = TestException)
                val actual = awaitItem()

                awaitComplete()

                Truth.assertThat(actual).isEqualTo(expected)
            }
    }

}
