package com.vlohachov.domain.usecase.movies

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class MovieDetailsUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<MovieDetailsUseCase.Param, MovieDetails>(coroutineContext = coroutineContext) {

    data class Param(
        val id: Int,
        val language: String? = null,
    )

    override fun execute(param: Param): Flow<MovieDetails> {
        return repository.getMovieDetails(id = param.id, language = param.language)
    }
}