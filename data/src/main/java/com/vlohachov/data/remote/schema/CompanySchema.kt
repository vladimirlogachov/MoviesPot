package com.vlohachov.data.remote.schema

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.shared.domain.model.Company

data class CompanySchema(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("origin_country")
    val originCountry: String,
)

internal fun CompanySchema.toDomain(): Company =
    Company(
        id = id,
        name = name,
        logoPath = TmdbConfig.BASE_IMAGE_URL + "w92" + logoPath,
        originCountry = originCountry,
    )
