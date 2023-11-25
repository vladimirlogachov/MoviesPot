package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class MovieKeywordsUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MovieRepository,
) : UseCase<MovieKeywordsUseCase.Param, List<Keyword>>(coroutineContext = coroutineContext) {

    data class Param(val id: Long)

    override fun execute(param: Param): Flow<List<Keyword>> {
        return repository.getMovieKeywords(id = param.id)
    }
}
