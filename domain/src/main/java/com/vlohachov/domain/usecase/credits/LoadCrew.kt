package com.vlohachov.domain.usecase.credits

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadCrew(
    private val repository: MovieRepository
) : UseCase<LoadCrew.Param, List<CrewMember>> {

    data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<List<CrewMember>>> =
        repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits -> credits.crew }
            .asResult()

}
