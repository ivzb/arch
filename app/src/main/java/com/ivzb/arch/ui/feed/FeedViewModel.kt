package com.ivzb.arch.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.R
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.Result.Loading
import com.ivzb.arch.domain.archive.LoadArchiveUseCase
import com.ivzb.arch.domain.successOr
import com.ivzb.arch.model.Archive
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
    loadArchiveUseCase: LoadArchiveUseCase
) : ViewModel() {

    val feed: LiveData<List<Any>>

    val errorMessage: LiveData<Event<String>>

    val snackBarMessage: LiveData<Event<SnackbarMessage>>

    private val loadArchiveResult = MutableLiveData<Result<List<Archive>>>()

    init {
        loadArchiveUseCase(Unit, loadArchiveResult)
        val archive: LiveData<List<Any>> = loadArchiveResult.map {
            if (it is Loading) {
                listOf(LoadingIndicator)
            } else {
                val items = it.successOr(emptyList())
                if (items.isNotEmpty()) items else listOf(ArchiveEmpty)
            }
        }

        // Compose feed
        feed = MutableLiveData(mutableListOf(SectionHeader(R.string.feed_archive_title)))
            .combine(archive) { header, archiveItems ->
                val feedItems = mutableListOf<Any>()

                feedItems.plus(header)
                    .plus(archiveItems)
            }

        errorMessage = loadArchiveResult.map {
            Event(content = (it as? Result.Error)?.exception?.message ?: "")
        }

        // Show an error message if the feed could not be loaded.
        snackBarMessage = MediatorLiveData()
        snackBarMessage.addSource(loadArchiveResult) {
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
}
