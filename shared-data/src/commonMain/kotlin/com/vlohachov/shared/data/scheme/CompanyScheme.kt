package com.vlohachov.shared.data.scheme

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.domain.model.Company
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CompanyScheme(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("origin_country")
    val originCountry: String,
)

internal fun CompanyScheme.toDomain(): Company =
    Company(
        id = id,
        name = name,
        logoPath = TmdbConfig.BASE_IMAGE_URL + "w92" + logoPath,
        originCountry = originCountry,
    )
