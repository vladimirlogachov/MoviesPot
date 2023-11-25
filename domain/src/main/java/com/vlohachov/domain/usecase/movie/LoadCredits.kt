package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class LoadCredits(
    private val repository: MovieRepository,
) : UseCase<LoadCredits.Param, MovieCredits> {

    data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<MovieCredits>> =
        repository.getMovieCredits(id = param.id, language = param.language)
            .asResult()

}
