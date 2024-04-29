package com.vlohachov.shared.data.scheme.genre

import com.vlohachov.shared.domain.model.genre.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenresScheme(
    @SerialName("genres")
    val genres: List<GenreScheme>
)

internal fun GenresScheme.toDomain(): List<Genre> =
    genres.map(GenreScheme::toDomain)
