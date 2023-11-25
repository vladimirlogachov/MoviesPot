package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class LoadKeywords(
    private val repository: MovieRepository,
) : UseCase<LoadKeywords.Param, List<Keyword>> {

    data class Param(val id: Long)

    override fun invoke(param: Param): Flow<Result<List<Keyword>>> =
        repository.getMovieKeywords(id = param.id)
            .asResult()

}
