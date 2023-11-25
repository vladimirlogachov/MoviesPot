package com.vlohachov.domain.usecase

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow

class DiscoverMovies(
    private val repository: DiscoverRepository,
) : UseCase<DiscoverMovies.Param, PaginatedData<Movie>> {

    private companion object {
        const val SEPARATOR = ","
    }

    data class Param(
        val page: Int = 1,
        val year: Int? = null,
        val genres: List<Int>? = null,
        val keywords: List<Int>? = null,
        val language: String? = null,
    )

    override fun invoke(param: Param): Flow<Result<PaginatedData<Movie>>> =
        repository.discoverMovies(
            page = param.page,
            year = param.year,
            genres = param.genres?.joinToString(separator = SEPARATOR),
            keywords = param.keywords?.joinToString(separator = SEPARATOR),
            language = param.language,
        ).asResult()
}
