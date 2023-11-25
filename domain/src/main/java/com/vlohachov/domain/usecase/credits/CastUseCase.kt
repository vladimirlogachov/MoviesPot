package com.vlohachov.domain.usecase.credits

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class CastUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MovieRepository,
) : UseCase<CastUseCase.Param, List<CastMember>>(coroutineContext = coroutineContext) {

    data class Param(val id: Long, val language: String? = null)

    override fun execute(param: Param): Flow<List<CastMember>> {
        return repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits -> credits.cast }
    }
}
