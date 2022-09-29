package com.vlohachov.data.remote.schema.movie.credit

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.movie.MovieCredits

data class MovieCreditsSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("cast")
    val cast: List<CastMemberSchema>,
    @SerializedName("crew")
    val crew: List<CrewMemberSchema>,
)

internal fun MovieCreditsSchema.toDomain(): MovieCredits =
    MovieCredits(
        id = id,
        cast = cast.map(CastMemberSchema::toDomain),
        crew = crew.map(CrewMemberSchema::toDomain),
    )
