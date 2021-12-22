package com.ivzb.arch.ui.search

data class SearchResult(
    val id: Int,
    val title: String?,
    val subtitle: String,
    val imageUrl: String?,
    val sitename: String?,
    val type: SearchResultType,
    val category: String
)

enum class SearchResultType {
    LINK
}
