package com.vlohachov.moviespot.usecase.credits

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.domain.usecase.credits.LoadDirector
import com.vlohachov.moviespot.data.TestCrewMember
import com.vlohachov.moviespot.data.TestMovieCredits
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoadDirectorTest {

    private companion object {
        val TestParam = LoadDirector.Param(id = 0)
        val Director = TestCrewMember.copy(
            name = "Director name",
            job = "Director",
        )
    }

    private val repository = mockk<MovieRepository>()

    private val useCase = LoadDirector(repository = repository)

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
    fun `result flow emits Success with director name`() = runTest {
        every {
            repository.getMovieCredits(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieCredits.copy(crew = listOf(Director)))

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = Director.name)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Success without director name`() = runTest {
        every {
            repository.getMovieCredits(
                id = any(),
                language = any(),
            )
        } returns flowOf(TestMovieCredits)

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = "")
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
