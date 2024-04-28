package com.vlohachov.shared.data.remote.scheme.movie.credit

import com.vlohachov.shared.domain.model.movie.MovieCredits
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class MovieCreditsSchema(
    @SerialName("id")
    val id: Long,
    @SerialName("cast")
    val cast: List<CastMemberSchema>,
    @SerialName("crew")
    val crew: List<CrewMemberSchema>,
)

internal fun MovieCreditsSchema.toDomain(): MovieCredits =
    MovieCredits(
        id = id,
        cast = cast.map(CastMemberSchema::toDomain),
        crew = crew.map(CrewMemberSchema::toDomain),
    )
