package com.vlohachov.shared.data.remote.scheme.movie.keyword

import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class KeywordSchema(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

internal fun KeywordSchema.toDomain(): Keyword =
    Keyword(id = id, name = name)
