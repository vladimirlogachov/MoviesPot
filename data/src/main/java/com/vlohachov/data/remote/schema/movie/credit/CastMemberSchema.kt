package com.vlohachov.data.remote.schema.movie.credit

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.shared.domain.model.movie.credit.CastMember

data class CastMemberSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("character")
    val character: String,
    @SerializedName("profile_path")
    val profilePath: String,
)

internal fun CastMemberSchema.toDomain(): CastMember =
    CastMember(
        id = id,
        name = name,
        character = character,
        profilePath = TmdbConfig.BASE_IMAGE_URL + "w342" + profilePath,
    )
