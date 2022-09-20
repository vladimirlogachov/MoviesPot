package com.vlohachov.moviespot.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverMoviesUseCaseTest {

    private companion object {
        val TestParam = DiscoverMoviesUseCase.Param()
    }

    private val repository = mockk<MoviesRepository>()

    private val useCase = DiscoverMoviesUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
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
    fun `Result flow emits Success`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
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
    fun `Result flow emits Error`() = runTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } returns flow { throw Exception() }

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }
}