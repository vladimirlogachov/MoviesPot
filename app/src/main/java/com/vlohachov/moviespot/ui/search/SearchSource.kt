package com.vlohachov.moviespot.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SearchSource(
    private val query: String,
    private val useCase: SearchMoviesUseCase,
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(anchorPosition = position)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val result = loadPage(page = page)
            LoadResult.Page(
                data = result.data,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (result.data.isEmpty()) null else result.page.plus(1),
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadPage(page: Int): PaginatedData<Movie> =
        useCase.resultFlow(param = SearchMoviesUseCase.Param(query = query, page = page))
            .filter { result -> result !is Result.Loading }
            .onEach { result -> if (result is Result.Error) throw result.exception }
            .map { result -> (result as Result.Success).value }
            .first()
}