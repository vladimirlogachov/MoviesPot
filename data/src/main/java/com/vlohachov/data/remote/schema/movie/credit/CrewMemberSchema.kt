package com.vlohachov.data.remote.schema.movie.credit

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.shared.domain.model.movie.credit.CrewMember

data class CrewMemberSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("job")
    val job: String,
    @SerializedName("profile_path")
    val profilePath: String,
)

internal fun CrewMemberSchema.toDomain(): CrewMember =
    CrewMember(
        id = id,
        name = name,
        job = job,
        profilePath = TmdbConfig.BASE_IMAGE_URL + "w342" + profilePath,
    )
