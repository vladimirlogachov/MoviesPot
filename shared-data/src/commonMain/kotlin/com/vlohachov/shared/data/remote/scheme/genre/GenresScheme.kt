package com.vlohachov.shared.data.remote.scheme.genre

import com.vlohachov.shared.domain.model.genre.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenresSchema(
    @SerialName("genres")
    val genres: List<GenreSchema>
)

internal fun GenresSchema.toDomain(): List<Genre> =
    genres.map(GenreSchema::toDomain)
