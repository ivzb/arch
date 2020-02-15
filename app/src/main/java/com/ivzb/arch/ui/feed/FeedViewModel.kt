package com.ivzb.arch.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.R
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.Result.Loading
import com.ivzb.arch.domain.links.DeleteLinkUseCase
import com.ivzb.arch.domain.links.LoadLinksUseCase
import com.ivzb.arch.domain.successOr
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.SectionHeader
import com.ivzb.arch.util.SnackbarMessage
import com.ivzb.arch.util.combine
import com.ivzb.arch.util.map
import javax.inject.Inject

/**
 * Loads data and exposes it to the view.
 * By annotating the constructor with [@Inject], Dagger will use that constructor when needing to
 * create the object, so defining a [@Provides] method for this class won't be needed.
 */
class FeedViewModel @Inject constructor(
    val loadLinksUseCase: LoadLinksUseCase,
    val deleteLinkUseCase: DeleteLinkUseCase
) : ViewModel(), EventActions {

    val feed: LiveData<List<Any>>

    val errorMessage: LiveData<Event<String>>

    val snackBarMessage: LiveData<Event<SnackbarMessage>>

    val performClickEvent: MutableLiveData<Event<Link>> = MutableLiveData()

    val deleteLinkResult = MutableLiveData<Result<Unit>>()

    private val loadLinksResult = MutableLiveData<Result<List<Link>>>()

    init {
        loadLinks()

        val links: LiveData<List<Any>> = loadLinksResult.map {
            if (it is Loading) {
                listOf(LoadingIndicator)
            } else {
                val items = it.successOr(emptyList())
                if (items.isNotEmpty()) items else listOf(LinkEmpty)
            }
        }

        // Compose feed

        feed = MutableLiveData(mutableListOf(SectionHeader(R.string.feed_links_title)))
            .combine(links) { header, linkItems ->
                val feedItems = mutableListOf<Any>()

                feedItems.plus(header)
                    .plus(linkItems)
            }

        errorMessage = loadLinksResult.map {
            Event(content = (it as? Result.Error)?.exception?.message ?: "")
        }

        // Show an error message if the feed could not be loaded.
        snackBarMessage = MediatorLiveData()
        snackBarMessage.addSource(loadLinksResult) {
            if (it is Result.Error) {
                snackBarMessage.value =
                    Event(
                        SnackbarMessage(
                            messageId = R.string.feed_loading_error,
                            longDuration = true
                        )
                    )
            }
        }
    }

    override fun click(link: Link) {
        performClickEvent.postValue(Event(link))
    }

    fun loadLinks() {
        loadLinksUseCase(Unit, loadLinksResult)
    }

    fun deleteLink(link: Link) {
        deleteLinkUseCase(link, deleteLinkResult)
    }
}
