package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.Genre
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class GenresUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<GenresUseCase.Param, List<Genre>>(coroutineContext = coroutineContext) {

    data class Param(val language: String? = null)

    override fun execute(param: Param): Flow<List<Genre>> {
        return repository.getGenres(language = param.language)
    }
}