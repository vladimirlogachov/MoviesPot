package com.vlohachov.domain.usecase.movies

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class MovieRecommendationsUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<MovieRecommendationsUseCase.Param, PaginatedData<Movie>>(coroutineContext = coroutineContext) {

    data class Param(
        val id: Long,
        val page: Int = 1,
        val language: String? = null,
    )

    override fun execute(param: Param): Flow<PaginatedData<Movie>> {
        return repository.getMovieRecommendations(
            id = param.id,
            page = param.page,
            language = param.language,
        )
    }
}