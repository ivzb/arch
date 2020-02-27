package com.ivzb.arch.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.links.LoadLinkUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.util.SnackbarMessage
import javax.inject.Inject
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.links.DeleteLinkUseCase
import com.ivzb.arch.domain.links.ObserveLinkUseCase

/**
 * Loads [Link] data and exposes it to the link detail view.
 */
class DetailsViewModel @Inject constructor(
    private val observeLinkUseCase: ObserveLinkUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val snackbarMessageManager: SnackbarMessageManager
) : ViewModel() {

    private val loadLinkResult by lazy(LazyThreadSafetyMode.NONE) {
        observeLinkUseCase.observe()
    }

    private val _errorMessage = MediatorLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _snackBarMessage = MediatorLiveData<Event<SnackbarMessage>>()
    val snackBarMessage: LiveData<Event<SnackbarMessage>>
        get() = _snackBarMessage

    private val _link = MediatorLiveData<Link>()
    val link: LiveData<Link>
        get() = _link

    init {
        _link.addSource(loadLinkResult) {
            (loadLinkResult.value as? Result.Success)?.data?.let {
                _link.value = it
            }
        }

    }

    fun observeLink(id: Int) {
        observeLinkUseCase.execute(id)
    }

    fun deleteLink(link: Link) {
        deleteLinkUseCase(link)
    }
}
