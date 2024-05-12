package com.vlohachov.shared.ui.screen.movies.similar

import androidx.paging.PagingSource
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

class SimilarMoviesSourceTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val useCase = LoadRecommendations(repository = repository)

    @Test
    fun `loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page<Int, Movie>(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )
        expect(expected = expected) {
            SimilarMoviesSource(movieId = 1, useCase = useCase)
                .load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 1,
                        placeholdersEnabled = false,
                    )
                )
        }
    }

    @Test
    @JsName(name = "loading_error")
    fun `loading error`() = runTest {
        val exception = IllegalStateException()
        val expected = PagingSource.LoadResult.Error<Int, Movie>(throwable = exception)

        resetAnswers(repository)

        every {
            repository.getMovieRecommendations(id = any(), page = any(), language = any())
        } throws exception

        expect(expected = expected) {
            SimilarMoviesSource(movieId = 1, useCase = useCase)
                .load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 1,
                        placeholdersEnabled = false,
                    )
                )
        }
    }

}
