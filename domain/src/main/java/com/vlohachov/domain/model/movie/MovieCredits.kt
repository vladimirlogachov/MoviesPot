package com.vlohachov.domain.model.movie

import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.domain.model.movie.credit.CrewMember

data class MovieCredits(
    val id: Long,
    val cast: List<CastMember>,
    val crew: List<CrewMember>,
)
