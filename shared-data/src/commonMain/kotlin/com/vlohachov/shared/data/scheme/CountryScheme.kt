package com.vlohachov.shared.data.scheme

import com.vlohachov.shared.domain.model.Country
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CountryScheme(
    @SerialName("name")
    val name: String,
    @SerialName("iso_3166_1")
    val iso: String,
)

internal fun CountryScheme.toDomain(): Country =
    Country(name = name, iso = iso)
