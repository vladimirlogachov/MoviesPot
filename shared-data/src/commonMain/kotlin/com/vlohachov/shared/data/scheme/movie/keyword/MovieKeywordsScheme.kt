package com.vlohachov.shared.data.scheme.movie.keyword

import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieKeywordsScheme(
    @SerialName("id")
    val id: Long,
    @SerialName("keywords")
    val keywords: List<KeywordScheme>,
)

internal fun MovieKeywordsScheme.toDomain(): List<Keyword> =
    keywords.map(KeywordScheme::toDomain)
