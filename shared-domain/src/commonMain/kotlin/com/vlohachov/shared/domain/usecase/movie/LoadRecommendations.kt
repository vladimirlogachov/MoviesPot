package com.vlohachov.shared.domain.usecase.movie

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

public class LoadRecommendations(
    private val repository: MovieRepository,
) : UseCase<LoadRecommendations.Param, PaginatedData<Movie>> {

    public data class Param(
        val id: Long,
        val page: Int = 1,
        val language: String? = null,
    )

    override fun invoke(param: Param): Flow<Result<PaginatedData<Movie>>> =
        repository.getMovieRecommendations(
            id = param.id,
            page = param.page,
            language = param.language,
        ).asResult()

}
