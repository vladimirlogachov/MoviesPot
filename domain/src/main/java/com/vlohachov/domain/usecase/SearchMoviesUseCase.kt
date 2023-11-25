package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class SearchMoviesUseCase(
    coroutineContext: CoroutineContext,
    private val repository: SearchRepository,
) : UseCase<SearchMoviesUseCase.Param, PaginatedData<Movie>>(coroutineContext = coroutineContext) {

    data class Param(val query: String, val page: Int, val language: String? = null)

    override fun execute(param: Param): Flow<PaginatedData<Movie>> {
        return repository.searchMovies(
            query = param.query,
            page = param.page,
            language = param.language,
        )
    }
}
