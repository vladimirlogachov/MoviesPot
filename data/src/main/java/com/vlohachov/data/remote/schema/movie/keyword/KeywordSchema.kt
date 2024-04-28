package com.vlohachov.data.remote.schema.movie.keyword

import com.google.gson.annotations.SerializedName
import com.vlohachov.shared.domain.model.movie.keyword.Keyword

data class KeywordSchema(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)

internal fun KeywordSchema.toDomain(): Keyword =
    Keyword(id = id, name = name)
