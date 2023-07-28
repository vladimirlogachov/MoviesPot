package com.vlohachov.moviespot.ui.movies.similar

import androidx.paging.PagingSource
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import com.vlohachov.moviespot.data.TestPaginatedData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SimilarMoviesSourceTest {

    private val useCase = mockk<MovieRecommendationsUseCase>()

    @Test
    fun `movies loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )

        every { useCase.resultFlow(param = any()) } returns flowOf(Result.Success(value = TestPaginatedData))

        val actual = SimilarMoviesSource(movieId = 1, useCase = useCase)
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

        val actual = SimilarMoviesSource(movieId = 1, useCase = useCase)
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
