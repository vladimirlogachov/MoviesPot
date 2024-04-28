package com.vlohachov.data.remote.schema

import com.google.gson.annotations.SerializedName
import com.vlohachov.shared.domain.model.Country

data class CountrySchema(
    @SerializedName("name")
    val name: String,
    @SerializedName("iso_3166_1")
    val iso: String,
)

internal fun CountrySchema.toDomain(): Country =
    Country(name = name, iso = iso)
