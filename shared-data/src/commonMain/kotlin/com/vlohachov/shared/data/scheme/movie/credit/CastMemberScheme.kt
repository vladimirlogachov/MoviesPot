package com.vlohachov.shared.data.scheme.movie.credit

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CastMemberScheme(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("character")
    val character: String,
    @SerialName("profile_path")
    val profilePath: String,
)

internal fun CastMemberScheme.toDomain(): CastMember =
    CastMember(
        id = id,
        name = name,
        character = character,
        profilePath = TmdbConfig.BASE_IMAGE_URL + "w342" + profilePath,
    )
