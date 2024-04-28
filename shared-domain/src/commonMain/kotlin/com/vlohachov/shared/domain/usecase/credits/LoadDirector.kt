package com.vlohachov.shared.domain.usecase.credits

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class LoadDirector(
    private val repository: MovieRepository,
) : UseCase<LoadDirector.Param, String> {

    private companion object {
        const val JOB_DIRECTOR = "Director"
    }

    public data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<String>> =
        repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits -> credits.crew }
            .map { crew -> crew.firstOrNull { member -> member.job == JOB_DIRECTOR } }
            .map { director -> director?.name.orEmpty() }
            .asResult()

}
