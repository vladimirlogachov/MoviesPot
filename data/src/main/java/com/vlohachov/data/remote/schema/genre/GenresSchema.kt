package com.vlohachov.data.remote.schema.genre

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.genre.Genre

data class GenresSchema(
    @SerializedName("genres")
    val genres: List<GenreSchema>
)

internal fun GenresSchema.toDomain(): List<Genre> =
    genres.map(GenreSchema::toDomain)