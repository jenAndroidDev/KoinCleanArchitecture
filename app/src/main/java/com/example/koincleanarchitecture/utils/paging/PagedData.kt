package com.pepul.shopsseller.utils.paging



data class PagedData<T>(
    val data: List<T>,
    val totalCount: Int,
    val prevKey: String?,
    val nextKey: String?
)