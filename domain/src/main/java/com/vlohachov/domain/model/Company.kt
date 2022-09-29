package com.vlohachov.domain.model

data class Company(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String,
)
