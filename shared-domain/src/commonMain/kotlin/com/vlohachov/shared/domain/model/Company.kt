package com.vlohachov.shared.domain.model

public data class Company(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String,
)
