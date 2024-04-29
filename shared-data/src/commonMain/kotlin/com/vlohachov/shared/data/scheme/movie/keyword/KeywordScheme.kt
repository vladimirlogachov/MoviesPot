package com.vlohachov.shared.data.scheme.movie.keyword

import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class KeywordScheme(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

internal fun KeywordScheme.toDomain(): Keyword =
    Keyword(id = id, name = name)
