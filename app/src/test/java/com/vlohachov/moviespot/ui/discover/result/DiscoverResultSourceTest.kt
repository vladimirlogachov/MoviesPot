package com.vlohachov.moviespot.ui.discover.result

import androidx.paging.PagingSource
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverResultSourceTest {

    private val useCase = mockk<DiscoverMoviesUseCase>()

    @Test
    fun `movies empty params loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page(
            data = listOf(),
            prevKey = null,
            nextKey = null,
        )

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Success(value = TestPaginatedData))

        val actual = DiscoverResultSource(year = null, selectedGenres = null, useCase = useCase)
            .load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false,
                )
            )

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `movies non-empty params loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Success(value = TestPaginatedData))

        val actual = DiscoverResultSource(
            year = 2022,
            selectedGenres = listOf(10),
            useCase = useCase,
        ).load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false,
            )
        )

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `movies loading error`() = runTest {
        val exception = Exception()

        val expected = PagingSource.LoadResult.Error<Int, Movie>(throwable = exception)

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Error(exception = exception))

        val actual = DiscoverResultSource(
            year = 2022,
            selectedGenres = listOf(10),
            useCase = useCase,
        ).load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false,
            )
        )

        Truth.assertThat(actual).isEqualTo(expected)
    }
}
