package com.vlohachov.domain.usecase

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchMovies(
    private val repository: SearchRepository,
) : UseCase<SearchMovies.Param, PaginatedData<Movie>> {

    data class Param(val query: String, val page: Int, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<PaginatedData<Movie>>> =
        repository.searchMovies(
            query = param.query,
            page = param.page,
            language = param.language,
        ).asResult()

}
