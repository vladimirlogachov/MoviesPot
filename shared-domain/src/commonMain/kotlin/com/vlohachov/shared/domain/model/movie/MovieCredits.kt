package com.vlohachov.shared.domain.model.movie

import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.domain.model.movie.credit.CrewMember

public data class MovieCredits(
    val id: Long,
    val cast: List<CastMember>,
    val crew: List<CrewMember>,
)
