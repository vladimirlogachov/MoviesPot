package com.vlohachov.shared.data.remote.scheme

import com.vlohachov.shared.domain.model.Language
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LanguageSchema(
    @SerialName("name")
    val name: String,
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso: String,
)

internal fun LanguageSchema.toDomain(): Language =
    Language(name = name, englishName = englishName, iso = iso)
