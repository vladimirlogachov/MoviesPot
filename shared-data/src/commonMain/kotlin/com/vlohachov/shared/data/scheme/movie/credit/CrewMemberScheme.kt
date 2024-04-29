package com.vlohachov.shared.data.scheme.movie.credit

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CrewMemberScheme(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("job")
    val job: String,
    @SerialName("profile_path")
    val profilePath: String,
)

internal fun CrewMemberScheme.toDomain(): CrewMember =
    CrewMember(
        id = id,
        name = name,
        job = job,
        profilePath = TmdbConfig.BASE_IMAGE_URL + "w342" + profilePath,
    )
