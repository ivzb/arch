package com.ivzb.arch.ui.link

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * ViewModel for link options dialog
 */
class LinkOptionsViewModel @Inject constructor() : ViewModel() {

    val url = MutableLiveData<String>()

    fun setUrl(value : String) {
        url.value = value
    }

    fun share() {

    }

    fun delete() {

    }
}
