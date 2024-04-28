package com.vlohachov.shared.domain.usecase.movie

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

public class LoadMoviesByCategory(
    private val repository: MovieRepository
) : UseCase<LoadMoviesByCategory.Param, PaginatedData<Movie>> {

    public data class Param(
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
