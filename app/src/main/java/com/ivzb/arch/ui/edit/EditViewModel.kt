package com.ivzb.arch.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.links.FetchLinkMetaDataUseCase
import com.ivzb.arch.domain.links.UpdateLinkUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.util.extractUrl
import javax.inject.Inject

class EditViewModel @Inject constructor(
    private val updateLinkUseCase: UpdateLinkUseCase,
    private val fetchLinkMetaDataUseCase: FetchLinkMetaDataUseCase,
    private val snackbarMessageManager: SnackbarMessageManager
) : ViewModel() {

    private val _canEditLink = MediatorLiveData<Boolean>()
    val canEditLink: LiveData<Boolean> = _canEditLink

    fun typeUrl(value: String) {
        _canEditLink.postValue(value.isNotEmpty())
    }

    fun editLink(id: Int, value: String) {
        Link(id = id, url = extractUrl(value)).let {
            updateLinkUseCase(it)
            fetchLinkMetaDataUseCase(it)
        }
    }
}
