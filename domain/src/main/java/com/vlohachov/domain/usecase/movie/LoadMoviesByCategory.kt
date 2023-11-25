package com.vlohachov.domain.usecase.movie

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCategory
import com.vlohachov.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class LoadMoviesByCategory(
    private val repository: MovieRepository
) : UseCase<LoadMoviesByCategory.Param, PaginatedData<Movie>> {

    data class Param(
        val category: MovieCategory,
        val page: Int = 1,
        val language: String? = null,
        val region: String? = null,
    )

    override operator fun invoke(param: Param): Flow<Result<PaginatedData<Movie>>> =
        repository.getMoviesByCategory(
            category = param.category.name.lowercase(),
            page = param.page,
            language = param.language,
            region = param.region,
        ).asResult()

}
