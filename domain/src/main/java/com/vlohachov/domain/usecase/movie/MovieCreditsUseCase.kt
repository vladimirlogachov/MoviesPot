package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class MovieCreditsUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<MovieCreditsUseCase.Param, MovieCredits>(coroutineContext = coroutineContext) {

    data class Param(val id: Long, val language: String? = null)

    override fun execute(param: Param): Flow<MovieCredits> {
        return repository.getMovieCredits(id = param.id, language = param.language)
    }
}
