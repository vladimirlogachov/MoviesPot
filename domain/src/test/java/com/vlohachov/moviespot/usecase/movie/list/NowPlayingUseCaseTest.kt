package com.vlohachov.moviespot.usecase.movie.list

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
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
class NowPlayingUseCaseTest {

    private companion object {
        val TestParam = NowPlayingUseCase.Param()
    }

    private val repository = mockk<MoviesRepository>()

    private val useCase = NowPlayingUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        every {
            repository.getNowPlayingMovies(
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
    fun `Result flow emits Success`() = runTest {
        every {
            repository.getNowPlayingMovies(
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
    fun `Result flow emits Error`() = runTest {
        every {
            repository.getNowPlayingMovies(
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
