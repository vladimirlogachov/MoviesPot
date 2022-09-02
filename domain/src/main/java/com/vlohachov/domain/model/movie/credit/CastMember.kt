package com.vlohachov.domain.model.movie.credit

data class CastMember(
    val id: Long,
    val name: String,
    val character: String,
    val profilePath: String,
)
