package com.vlohachov.shared.ui.screen.search

import androidx.paging.PagingSource
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.SearchRepository
import com.vlohachov.shared.domain.usecase.SearchMovies
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

class MoviesSearchSourceTest {

    private val repository = mock<SearchRepository> {
        every {
            searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val useCase = SearchMovies(repository = repository)

    @Test
    @JsName(name = "empty_query_loading_success")
    fun `empty query loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page<Int, Movie>(
            data = listOf(),
            prevKey = null,
            nextKey = null,
        )

        expect(expected = expected) {
            MoviesSearchSource(query = "", useCase = useCase)
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
    @JsName(name = "non_empty_query_loading_success")
    fun `non-empty query loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page<Int, Movie>(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )

        expect(expected = expected) {
            MoviesSearchSource(query = "test", useCase = useCase)
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
            repository.searchMovies(query = any(), page = any(), language = any())
        } throws exception

        expect(expected = expected) {
            MoviesSearchSource(query = "test", useCase = useCase)
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
