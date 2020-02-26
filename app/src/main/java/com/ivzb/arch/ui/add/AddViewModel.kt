package com.ivzb.arch.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.links.FetchLinkMetaDataUseCase
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.ui.search.SearchResult
import com.ivzb.arch.util.extractUrl
import javax.inject.Inject

class AddViewModel @Inject constructor(
    private val insertLinkUseCase: InsertLinkUseCase,
    private val fetchLinkMetaDataUseCase: FetchLinkMetaDataUseCase,
    private val snackbarMessageManager: SnackbarMessageManager
) : ViewModel() {

    private val _canAddLink = MediatorLiveData<Boolean>()
    val canAddLink: LiveData<Boolean> = _canAddLink

    fun typeUrl(value: String) {
        _canAddLink.postValue(value.isNotEmpty())
    }

    fun addLink(value: String) {
        Link(url = extractUrl(value)).let {
            insertLinkUseCase(it)
            fetchLinkMetaDataUseCase(it)
        }
    }
}
