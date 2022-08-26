package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class TopRatedUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository
) : UseCase<TopRatedUseCase.Param, List<Movie>>(coroutineContext = coroutineContext) {

    data class Param(
        val page: Int = 1,
        val language: String? = null,
        val region: String? = null,
    )

    override fun execute(param: Param): Flow<List<Movie>> {
        return repository.getTopRatedMovies(
            page = param.page,
            language = param.language,
            region = param.region,
        )
    }
}