package com.ivzb.arch.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.BuildConfig
import com.ivzb.arch.R
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.Result.Loading
import com.ivzb.arch.domain.announcements.AnnouncementsParameters
import com.ivzb.arch.domain.announcements.LoadAnnouncementsUseCase
import com.ivzb.arch.domain.links.FetchLinkMetaDataUseCase
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.domain.links.ObserveLinksUseCase
import com.ivzb.arch.domain.successOr
import com.ivzb.arch.model.Announcement
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.SectionHeader
import com.ivzb.arch.util.SnackbarMessage
import com.ivzb.arch.util.combine
import com.ivzb.arch.util.extractUrl
import com.ivzb.arch.util.map
import javax.inject.Inject

/**
 * Loads data and exposes it to the view.
 * By annotating the constructor with [@Inject], Dagger will use that constructor when needing to
 * create the object, so defining a [@Provides] method for this class won't be needed.
 */
class FeedViewModel @Inject constructor(
    loadAnnouncementsUseCase: LoadAnnouncementsUseCase,
    private val observeLinksUseCase: ObserveLinksUseCase,
    private val insertLinkUseCase: InsertLinkUseCase,
    private val fetchLinkMetaDataUseCase: FetchLinkMetaDataUseCase
) : ViewModel(), EventActions {

    val feed: LiveData<List<Any>>

    val searchVisible: LiveData<Event<Boolean>>

    val errorMessage: LiveData<Event<String>>

    val snackBarMessage: LiveData<Event<SnackbarMessage>>

    val performLinkClickEvent: MutableLiveData<Event<Link>> = MutableLiveData()

    val performLinkLongClickEvent: MutableLiveData<Event<Link>> = MutableLiveData()

    private val loadAnnouncementsResult = MutableLiveData<Result<List<Announcement>>>()

    private val loadLinksResult by lazy(LazyThreadSafetyMode.NONE) {
        observeLinksUseCase.observe()
    }

    init {
        val links = loadLinksResult.map {
            if (it is Loading) {
                listOf(LoadingIndicator)
            } else {
                val items = it.successOr(emptyList())

                val announcementsParameters = AnnouncementsParameters(
                    hasLinks = items.isNotEmpty(),
                    hasCategories = items.any { item -> item.category != "" },
                    appVersion = BuildConfig.VERSION_NAME
                )

                loadAnnouncementsUseCase(announcementsParameters, loadAnnouncementsResult)

                if (items.isNotEmpty()) items else listOf(LinkEmpty)
            }
        }

        val announcements: LiveData<List<Any>> = loadAnnouncementsResult.map {
            if (it is Loading) {
                listOf(LoadingIndicator)
            } else {
                it.successOr(emptyList())
            }
        }

        // Compose feed
        feed = announcements.combine(links) { announcementItems, linkItems ->

            val feedItems = mutableListOf<Any>()

            Log.d("debug_log", "announcements = ${announcementItems}")

            if (announcementItems.isNotEmpty()) {
                feedItems.add(SectionHeader("Announcements"))
                feedItems.addAll(announcementItems)
            }

            linkItems
                .groupBy {
                    if (it is Link) it.category.toLowerCase() else ""
                }
                .entries
                .sortedBy { it.key.toLowerCase() }
                .forEach { (category, links) ->
                    if (category != "") {
                        feedItems.add(SectionHeader(category))
                    } else {
                        feedItems.add(SectionHeader("Links"))
                    }

                    feedItems.addAll(links)
                }

            feedItems
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

        searchVisible = loadLinksResult.map {
            Event(content = (it as? Result.Success)?.data?.isNotEmpty() ?: false)
        }

        // Observe updates for links
        observeLinksUseCase.execute(Unit)
    }

    override fun click(link: Link) {
        performLinkClickEvent.postValue(Event(link))
    }

    override fun longClick(link: Link) {
        performLinkLongClickEvent.postValue(Event(link))
    }

    fun addLink(value: String) {
        Link(url = extractUrl(value), category = "").let {
            insertLinkUseCase(it)
            fetchLinkMetaDataUseCase(it)
        }
    }
}
