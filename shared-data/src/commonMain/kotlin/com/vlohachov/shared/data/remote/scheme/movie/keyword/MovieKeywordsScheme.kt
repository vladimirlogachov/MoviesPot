package com.vlohachov.shared.data.remote.scheme.movie.keyword

import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieKeywordsSchema(
    @SerialName("id")
    val id: Long,
    @SerialName("keywords")
    val keywords: List<KeywordSchema>,
)

internal fun MovieKeywordsSchema.toDomain(): List<Keyword> =
    keywords.map(KeywordSchema::toDomain)
