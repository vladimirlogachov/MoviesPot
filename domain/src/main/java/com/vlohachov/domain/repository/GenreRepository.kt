package com.vlohachov.domain.repository

import com.vlohachov.domain.model.genre.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

    fun getGenres(language: String?): Flow<List<Genre>>

}
