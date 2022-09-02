package com.vlohachov.data.remote.schema.movie.keyword

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.movie.keyword.Keyword

data class MovieKeywordsSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("keywords")
    val keywords: List<KeywordSchema>,
)

internal fun MovieKeywordsSchema.toDomain(): List<Keyword> =
    keywords.map(KeywordSchema::toDomain)
