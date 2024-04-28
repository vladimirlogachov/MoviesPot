package com.vlohachov.shared.domain.usecase

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

public class SearchMovies(
    private val repository: SearchRepository,
) : UseCase<SearchMovies.Param, PaginatedData<Movie>> {

    public data class Param(val query: String, val page: Int, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<PaginatedData<Movie>>> =
        repository.searchMovies(query = param.query, page = param.page, language = param.language)
            .asResult()

}
