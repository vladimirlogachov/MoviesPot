package com.vlohachov.data.remote.schema.movie.keyword

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.movie.keyword.Keyword

data class KeywordSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
)

internal fun KeywordSchema.toDomain(): Keyword =
    Keyword(id = id, name = name)
