package com.vlohachov.domain.usecase.credits

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class DirectorUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<DirectorUseCase.Param, String>(coroutineContext = coroutineContext) {

    private companion object {
        const val JOB_DIRECTOR = "Director"
    }

    data class Param(val id: Long, val language: String? = null)

    override fun execute(param: Param): Flow<String> {
        return repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits ->
                credits.crew
                    .find { member -> member.job == JOB_DIRECTOR }
                    ?.name.orEmpty()
            }
    }
}