package com.vlohachov.shared.domain.model

public data class PaginatedData<T>(
    val page: Int,
    val data: List<T>,
    val totalResults: Int,
    val totalPages: Int,
)
