package com.vlohachov.domain.usecase.credits

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadDirector(
    private val repository: MovieRepository,
) : UseCase<LoadDirector.Param, String> {

    private companion object {
        const val JOB_DIRECTOR = "Director"
    }

    data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<String>> =
        repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits -> credits.crew }
            .map { crew -> crew.firstOrNull { member -> member.job == JOB_DIRECTOR } }
            .map { director -> director?.name.orEmpty() }
            .asResult()

}
