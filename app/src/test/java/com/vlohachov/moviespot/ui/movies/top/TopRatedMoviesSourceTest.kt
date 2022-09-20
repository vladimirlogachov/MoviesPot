package com.vlohachov.moviespot.ui.movies.top

import androidx.paging.PagingSource
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatedMoviesSourceTest {

    private val useCase = mockk<TopRatedUseCase>()

    @Test
    fun `movies loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Success(value = TestPaginatedData))

        val actual = TopRatedMoviesSource(useCase = useCase)
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
    fun `movies loading error`() = runTest {
        val exception = Exception()

        val expected = PagingSource.LoadResult.Error<Int, Movie>(throwable = exception)

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Error(exception = exception))

        val actual = TopRatedMoviesSource(useCase = useCase)
            .load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false,
                )
            )

        Truth.assertThat(actual).isEqualTo(expected)
    }
}