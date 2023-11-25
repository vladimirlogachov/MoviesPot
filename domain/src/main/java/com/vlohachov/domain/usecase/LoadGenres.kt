package com.vlohachov.domain.usecase

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.repository.GenreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class LoadGenres(
    private val repository: GenreRepository,
) : UseCase<LoadGenres.Param, List<Genre>> {

    data class Param(val count: Int = -1, val language: String? = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(param: Param): Flow<Result<List<Genre>>> =
        repository.getGenres(language = param.language)
            .mapLatest { genres ->
                if (param.count < 0) {
                    genres
                } else {
                    genres.take(n = param.count)
                }
            }
            .asResult()

}
