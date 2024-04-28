package com.vlohachov.shared.domain.repository

import com.vlohachov.shared.domain.model.genre.Genre
import kotlinx.coroutines.flow.Flow

public interface GenreRepository {

    public fun getGenres(language: String?): Flow<List<Genre>>

}
