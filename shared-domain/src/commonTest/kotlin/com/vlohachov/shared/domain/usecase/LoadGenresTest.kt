package com.vlohachov.shared.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.repository.GenreRepository
import com.vlohachov.moviespot.data.TestGenres
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoadGenresTest {

    private companion object {
        val TestParam = LoadGenres.Param()
    }

    private val repository = mockk<GenreRepository>()

    private val useCase = LoadGenres(repository = repository)

    @Test
    fun `result flow emits Loading`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam).test {
            val actual = awaitItem()

            skipItems(count = 1)
            awaitComplete()

            Truth.assertThat(actual is Result.Loading).isTrue()
        }
    }

    @Test
    fun `result flow emits Success with all genres`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestGenres)
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Success with n genres`() = runTest {
        val genresToTake = 1

        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam.copy(count = genresToTake)).test {
            skipItems(count = 1)

            val expected = Result.Success(value = TestGenres.take(n = genresToTake))
            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `result flow emits Error`() = runTest {
        every { repository.getGenres(language = any()) } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            skipItems(count = 1)

            val actual = awaitItem()

            awaitComplete()

            Truth.assertThat(actual is Result.Error).isTrue()
        }
    }

}
