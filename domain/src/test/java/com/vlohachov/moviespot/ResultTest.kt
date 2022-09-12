package com.vlohachov.moviespot

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun `Result flow emits Loading`() = runTest {
        testFlow.asResult().test {
            testFlow.emit(value = TestValue)

            val expected = Result.Loading
            val actual = awaitItem()

            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Success`() = runTest {
        testFlow.asResult().test {
            testFlow.emit(value = TestValue)

            skipItems(count = 1)

            val expected = Result.Success(value = TestValue)
            val actual = awaitItem()

            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Error`() = runTest {
        testFlow
            .onEach { throw TestException }
            .asResult()
            .test {
                testFlow.emit(value = TestValue)

                skipItems(count = 1)

                val expected = Result.Error(exception = TestException)
                val actual = awaitItem()

                awaitComplete()

                assertThat(actual).isEqualTo(expected)
            }
    }
}