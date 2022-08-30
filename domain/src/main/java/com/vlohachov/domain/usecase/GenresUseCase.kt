package com.vlohachov.domain.usecase

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.coroutines.CoroutineContext

class GenresUseCase(
    coroutineContext: CoroutineContext,
    private val repository: MoviesRepository,
) : UseCase<GenresUseCase.Param, List<Genre>>(coroutineContext = coroutineContext) {

    data class Param(val count: Int = -1, val language: String? = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(param: Param): Flow<List<Genre>> {
        return repository.getGenres(language = param.language)
            .mapLatest { genres ->
                if (param.count < 0) {
                    genres
                } else {
                    genres.take(param.count)
                }
            }
    }
}