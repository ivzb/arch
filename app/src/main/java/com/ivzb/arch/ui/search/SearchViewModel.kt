package com.ivzb.arch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.search.ObserveSearchUseCase
import com.ivzb.arch.domain.search.Searchable
import com.ivzb.arch.domain.successOr
import com.ivzb.arch.model.Link
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val observeSearchUseCase: ObserveSearchUseCase
) : ViewModel(), SearchResultActionHandler {

    private val loadSearchResults by lazy(LazyThreadSafetyMode.NONE) {
        observeSearchUseCase.observe()
    }

    private val _searchResults = MediatorLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _isEmpty = MediatorLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    val performLinkClickEvent: MutableLiveData<Event<Link>> = MutableLiveData()

    init {
        _searchResults.addSource(loadSearchResults) {
            val result = (it as? Result.Success)?.data ?: emptyList()
            _searchResults.value = result.map { searched ->
                when (searched) {
                    is Searchable.SearchedLink -> {
                        val link = searched.link

                        SearchResult(
                            id = link.id,
                            title = link.title,
                            subtitle= link.url,
                            imageUrl = link.imageUrl,
                            sitename = link.sitename,
                            type = SearchResultType.LINK
                        )
                    }
                }
            }
        }

        _isEmpty.addSource(loadSearchResults) {
            _isEmpty.value = it.successOr(null).isNullOrEmpty()
        }
    }

    override fun openSearchResult(searchResult: SearchResult) {
        when (searchResult.type) {
            SearchResultType.LINK -> {
                performLinkClickEvent.postValue(Event(Link(
                    id = searchResult.id,
                    title = searchResult.title,
                    url = searchResult.subtitle,
                    imageUrl = searchResult.imageUrl
                )))
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (query.length < 2) {
            onQueryCleared()
            return
        }

        observeSearchUseCase.execute(query)
    }

    private fun onQueryCleared() {
        _searchResults.value = emptyList()
        // Explicitly set false to not show the "No results" state
        _isEmpty.value = false
    }
}
