package com.vlohachov.domain.model

data class PaginatedData<T>(
    val page: Int,
    val data: List<T>,
    val totalResults: Int,
    val totalPages: Int,
)
