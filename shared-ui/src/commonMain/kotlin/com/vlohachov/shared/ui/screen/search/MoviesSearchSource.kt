package com.vlohachov.shared.ui.screen.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.usecase.SearchMovies
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class MoviesSearchSource(
    private val query: String,
    private val useCase: SearchMovies,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { position ->
            state.closestPageToPosition(anchorPosition = position)?.run {
                prevKey?.plus(other = 1) ?: nextKey?.minus(other = 1)
            }
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        if (query.isEmpty()) {
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        } else {
            runCatching { loadPage(page = params.key ?: 1) }
                .fold(
                    onSuccess = { result ->
                        LoadResult.Page(
                            data = result.data,
                            prevKey = result.prevKey(),
                            nextKey = result.nextKey(),
                        )
                    },
                    onFailure = { e -> LoadResult.Error(throwable = e) }
                )
        }

    private fun PaginatedData<Movie>.prevKey(): Int? =
        if (page == 1) null else page.minus(other = 1)

    private fun PaginatedData<Movie>.nextKey(): Int? =
        if (page >= totalPages) null else page.plus(other = 1)

    private suspend fun loadPage(page: Int): PaginatedData<Movie> =
        useCase(param = SearchMovies.Param(query = query, page = page))
            .filter { result -> result !is Result.Loading }
            .onEach { result -> if (result is Result.Error) throw result.exception }
            .map { result -> (result as Result.Success).value }
            .first()

}
