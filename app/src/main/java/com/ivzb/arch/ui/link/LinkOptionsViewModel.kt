package com.ivzb.arch.ui.link

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.links.DeleteLinkUseCase
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

enum class LinkOptionsEvent {
    Copy, Share, Delete, Visit
}

/**
 * ViewModel for link options dialog
 */
class LinkOptionsViewModel @Inject constructor(
    private val deleteLinkUseCase: DeleteLinkUseCase
) : ViewModel() {

    val link = MutableLiveData<Link>()

    val performLinkOptionEvent: MutableLiveData<Event<LinkOptionsEvent>> = MutableLiveData()

    fun setLink(value: Link) {
        link.value = value
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

    fun deleteLink(link: Link) {
        deleteLinkUseCase(link)
    }
}
