package com.vlohachov.shared.domain.usecase

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow

public class DiscoverMovies(
    private val repository: DiscoverRepository,
) : UseCase<DiscoverMovies.Param, PaginatedData<Movie>> {

    private companion object {
        const val SEPARATOR = ","
    }

    public data class Param(
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
