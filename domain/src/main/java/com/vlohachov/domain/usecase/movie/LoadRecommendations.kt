package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class LoadRecommendations(
    private val repository: MovieRepository,
) : UseCase<LoadRecommendations.Param, PaginatedData<Movie>> {

    data class Param(
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
