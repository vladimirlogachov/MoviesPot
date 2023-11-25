package com.vlohachov.moviespot.usecase.movie.list

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpcomingUseCaseTest {

    private companion object {
        val TestParam = UpcomingUseCase.Param()
    }

    private val repository = mockk<MovieRepository>()

    private val useCase = UpcomingUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `result flow emits Loading`() = runTest {
        every {
            repository.getUpcomingMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase.resultFlow(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Success`() = runTest {
        every {
            repository.getUpcomingMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase.resultFlow(param = TestParam).test {
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
            repository.getUpcomingMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flow { throw NullPointerException() }

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
