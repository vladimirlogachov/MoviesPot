package com.vlohachov.data.remote.schema

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.Language

data class LanguageSchema(
    @SerializedName("name")
    val name: String,
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val iso: String,
)

internal fun LanguageSchema.toDomain(): Language =
    Language(name = name, englishName = englishName, iso = iso)
