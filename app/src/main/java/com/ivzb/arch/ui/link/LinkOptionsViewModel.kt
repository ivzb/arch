package com.ivzb.arch.ui.link

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import javax.inject.Inject

enum class LinkOptionsEvent {
    Copy, Share, Delete, Visit
}

/**
 * ViewModel for link options dialog
 */
class LinkOptionsViewModel @Inject constructor() : ViewModel() {

    val url = MutableLiveData<String>()

    val performLinkOptionEvent: MutableLiveData<Event<LinkOptionsEvent>> = MutableLiveData()

    fun setUrl(value: String) {
        url.value = value
    }

    fun visit() {
        performLinkOptionEvent.postValue(Event(LinkOptionsEvent.Visit))
    }

    fun copy() {
        performLinkOptionEvent.postValue(Event(LinkOptionsEvent.Copy))
    }

    fun share() {
        performLinkOptionEvent.postValue(Event(LinkOptionsEvent.Share))
    }

    fun delete() {
        performLinkOptionEvent.postValue(Event(LinkOptionsEvent.Delete))
    }
}
