package com.vlohachov.moviespot.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.moviespot.data.TestGenres
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenresUseCaseTest {

    private companion object {
        val TestParam = GenresUseCase.Param()
    }

    private val repository = mockk<MoviesRepository>()

    private val useCase = GenresUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    @Test
    fun `Result flow emits Loading`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase.resultFlow(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `Result flow emits Success with all genres`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestGenres)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Success with n genres`() = runTest {
        val genresToTake = 1

        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase.resultFlow(param = TestParam.copy(count = genresToTake)).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestGenres.take(n = genresToTake))
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `Result flow emits Error`() = runTest {
        every { repository.getGenres(language = any()) } returns flow { throw NullPointerException() }

        useCase.resultFlow(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }
}
