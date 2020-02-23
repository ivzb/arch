package com.ivzb.arch.ui.search

/**
 * Actions that can be performed on a [SearchResult]
 */
interface SearchResultActionHandler {

    fun clickSearchResult(searchResult: SearchResult)

    fun longClickSearchResult(searchResult: SearchResult)
}
