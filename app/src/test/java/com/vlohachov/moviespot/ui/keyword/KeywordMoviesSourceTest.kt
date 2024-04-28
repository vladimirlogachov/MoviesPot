package com.vlohachov.moviespot.ui.keyword

import androidx.paging.PagingSource
import com.google.common.truth.Truth
import com.vlohachov.moviespot.data.TestPaginatedData
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.usecase.DiscoverMovies
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class KeywordMoviesSourceTest {

    private val useCase = mockk<DiscoverMovies>()

    @Test
    fun `movies loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )

        every { useCase(param = any()) } returns flowOf(Result.Success(value = TestPaginatedData))

        val actual = KeywordMoviesSource(keywordId = 0, useCase = useCase)
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

        every { useCase(param = any()) } returns flowOf(Result.Error(exception = exception))

        val actual = KeywordMoviesSource(keywordId = 0, useCase = useCase)
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
