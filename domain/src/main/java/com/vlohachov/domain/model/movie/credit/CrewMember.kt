package com.vlohachov.domain.model.movie.credit

data class CrewMember(
    val id: Long,
    val name: String,
    val job: String,
    val profilePath: String,
)