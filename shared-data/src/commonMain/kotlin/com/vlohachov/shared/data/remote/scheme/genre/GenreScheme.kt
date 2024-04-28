package com.vlohachov.shared.data.remote.scheme.genre

import com.vlohachov.shared.domain.model.genre.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreSchema(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

internal fun GenreSchema.toDomain(): Genre =
    Genre(id = id, name = name)
