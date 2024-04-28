package com.vlohachov.shared.domain.usecase.movie

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.moviespot.data.TestKeywords
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoadKeywordsTest {

    private companion object {
        val TestParam = LoadKeywords.Param(id = 0)
    }

    private val repository = mockk<MovieRepository>()

    private val useCase = LoadKeywords(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flowOf(TestKeywords)

        useCase(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Success`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flowOf(TestKeywords)

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestKeywords)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
