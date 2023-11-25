package com.vlohachov.moviespot.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.DiscoverRepository
import com.vlohachov.domain.usecase.DiscoverMovies
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DiscoverMoviesTest {

    private companion object {
        val TestParam = DiscoverMovies.Param()
    }

    private val repository = mockk<DiscoverRepository>()

    private val useCase = DiscoverMovies(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Success`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestPaginatedData)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
