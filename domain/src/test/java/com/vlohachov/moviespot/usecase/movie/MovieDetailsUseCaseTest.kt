package com.vlohachov.moviespot.usecase.movie

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.movie.MovieDetailsUseCase
import com.vlohachov.moviespot.data.TestMovieDetails
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsUseCaseTest {

    private companion object {
        val TestParam = MovieDetailsUseCase.Param(id = 0)
    }

    private val repository = mockk<MoviesRepository>()

    private val useCase = MovieDetailsUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        every {
            repository.getMovieDetails(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieDetails)

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
            repository.getMovieDetails(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieDetails)

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestMovieDetails)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Error`() = runTest {
        every {
            repository.getMovieDetails(
                id = any(),
                language = any(),
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
