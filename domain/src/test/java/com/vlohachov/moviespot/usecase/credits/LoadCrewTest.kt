package com.vlohachov.moviespot.usecase.credits

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.domain.usecase.credits.LoadCrew
import com.vlohachov.moviespot.data.TestMovieCredits
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoadCrewTest {

    private companion object {
        val TestParam = LoadCrew.Param(id = 0)
    }

    private val repository = mockk<MovieRepository>()

    private val useCase = LoadCrew(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every {
            repository.getMovieCredits(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieCredits)

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
            repository.getMovieCredits(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieCredits)

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestMovieCredits.crew)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        every {
            repository.getMovieCredits(
                id = any(),
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
