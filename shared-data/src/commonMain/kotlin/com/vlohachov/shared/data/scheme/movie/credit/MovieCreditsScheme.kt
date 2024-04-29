package com.vlohachov.shared.data.scheme.movie.credit

import com.vlohachov.shared.domain.model.movie.MovieCredits
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class MovieCreditsScheme(
    @SerialName("id")
    val id: Long,
    @SerialName("cast")
    val cast: List<CastMemberScheme>,
    @SerialName("crew")
    val crew: List<CrewMemberScheme>,
)

internal fun MovieCreditsScheme.toDomain(): MovieCredits =
    MovieCredits(
        id = id,
        cast = cast.map(CastMemberScheme::toDomain),
        crew = crew.map(CrewMemberScheme::toDomain),
    )
