package com.vlohachov.data.remote.schema.genre

import com.google.gson.annotations.SerializedName
import com.vlohachov.shared.domain.model.genre.Genre

data class GenreSchema(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)

internal fun GenreSchema.toDomain(): Genre =
    Genre(id = id, name = name)
