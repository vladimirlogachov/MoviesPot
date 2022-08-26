package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class UpcomingUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<UpcomingUseCase.Param, List<Movie>>(coroutineContext = coroutineContext) {

    data class Param(
        val page: Int = 1,
        val language: String? = null,
        val region: String? = null,
    )

    override fun execute(param: Param): Flow<List<Movie>> {
        return repository.getUpcomingMovies(
            page = param.page,
            language = param.language,
            region = param.region,
        )
    }
}