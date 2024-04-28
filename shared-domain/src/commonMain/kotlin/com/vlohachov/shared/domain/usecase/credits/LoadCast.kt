package com.vlohachov.shared.domain.usecase.credits

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class LoadCast(
    private val repository: MovieRepository,
) : UseCase<LoadCast.Param, List<CastMember>> {

    public data class Param(val id: Long, val language: String? = null)

    override fun invoke(param: Param): Flow<Result<List<CastMember>>> =
        repository.getMovieCredits(id = param.id, language = param.language)
            .map { credits -> credits.cast }
            .asResult()

}
