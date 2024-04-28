package com.vlohachov.shared.domain.usecase.movie

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

public class LoadDetails(
    private val repository: MovieRepository,
) : UseCase<LoadDetails.Param, MovieDetails> {

    public data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<MovieDetails>> =
        repository.getMovieDetails(id = param.id, language = param.language)
            .asResult()

}
