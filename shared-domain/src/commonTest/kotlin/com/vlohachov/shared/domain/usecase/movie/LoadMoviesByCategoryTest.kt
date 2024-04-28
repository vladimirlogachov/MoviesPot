package com.vlohachov.shared.domain.usecase.movie

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.MovieCategory
import com.vlohachov.domain.repository.MovieRepository
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoadMoviesByCategoryTest {

    private companion object {
        val TestParam = LoadMoviesByCategory.Param(category = MovieCategory.UPCOMING)
    }

    private val repository = mockk<MovieRepository>()

    private val useCase = LoadMoviesByCategory(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Success`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(TestParam).test {
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
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any(),
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
