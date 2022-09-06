package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class DiscoverMoviesUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<DiscoverMoviesUseCase.Param, PaginatedData<Movie>>(coroutineContext = coroutineContext) {

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

    override fun execute(param: Param): Flow<PaginatedData<Movie>> {
        return repository.discoverMovies(
            page = param.page,
            year = param.year,
            genres = param.genres?.joinToString(separator = SEPARATOR),
            keywords = param.keywords?.joinToString(separator = SEPARATOR),
            language = param.language,
        )
    }
}