package com.vlohachov.shared.ui.screen.keyword

import androidx.paging.PagingSource
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.DiscoverRepository
import com.vlohachov.shared.domain.usecase.DiscoverMovies
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

class KeywordMoviesSourceTest {

    private val repository = mock<DiscoverRepository> {
        every {
            discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flowOf(value = TestPaginatedData)
    }

    private val useCase = DiscoverMovies(repository = repository)

    @Test
    @JsName(name = "loading_success")
    fun `loading success`() = runTest {
        val expected = PagingSource.LoadResult.Page<Int, Movie>(
            data = TestPaginatedData.data,
            prevKey = null,
            nextKey = null,
        )
        expect(expected = expected) {
            KeywordMoviesSource(keywordId = 1, useCase = useCase)
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
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } throws exception

        expect(expected = expected) {
            KeywordMoviesSource(keywordId = 1, useCase = useCase)
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
