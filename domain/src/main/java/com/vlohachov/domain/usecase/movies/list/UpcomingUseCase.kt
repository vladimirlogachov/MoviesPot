package com.vlohachov.domain.usecase.movies.list

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class UpcomingUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<UpcomingUseCase.Param, PaginatedData<Movie>>(coroutineContext = coroutineContext) {

    data class Param(
        val page: Int = 1,
        val language: String? = null,
        val region: String? = null,
    )

    override fun execute(param: Param): Flow<PaginatedData<Movie>> {
        return repository.getUpcomingMovies(
            page = param.page,
            language = param.language,
            region = param.region,
        )
    }
}