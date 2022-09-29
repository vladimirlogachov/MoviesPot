package com.vlohachov.moviespot.ui.discover.result

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class DiscoverResultSource(
    private val year: Int?,
    private val selectedGenres: List<Int>?,
    private val useCase: DiscoverMoviesUseCase,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(anchorPosition = position)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return if (isValidParam()) {
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        } else try {
            val page = params.key ?: 1
            val result = loadPage(page = page)
            LoadResult.Page(
                data = result.data,
                prevKey = result.prevKey(),
                nextKey = result.nextKey(),
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    private fun isValidParam(): Boolean =
        year == null && selectedGenres == null

    private fun PaginatedData<Movie>.prevKey(): Int? =
        if (page == 1) null else page.minus(1)

    private fun PaginatedData<Movie>.nextKey(): Int? =
        if (page >= totalPages) null else page.plus(1)

    private suspend fun loadPage(page: Int): PaginatedData<Movie> {
        val param = DiscoverMoviesUseCase.Param(
            page = page,
            year = year,
            genres = selectedGenres,
        )
        return useCase.resultFlow(param = param)
            .filter { result -> result !is Result.Loading }
            .onEach { result -> if (result is Result.Error) throw result.exception }
            .map { result -> (result as Result.Success).value }
            .first()
    }
}
