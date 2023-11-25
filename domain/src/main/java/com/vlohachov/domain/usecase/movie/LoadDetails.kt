package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class LoadDetails(
    private val repository: MovieRepository,
) : UseCase<LoadDetails.Param, MovieDetails> {

    data class Param(
        val id: Long,
        val language: String? = null,
    )

    override fun invoke(param: Param): Flow<Result<MovieDetails>> =
        repository.getMovieDetails(id = param.id, language = param.language)
            .asResult()

}
