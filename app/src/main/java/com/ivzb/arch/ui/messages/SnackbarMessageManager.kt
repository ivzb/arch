package com.ivzb.arch.ui.messages

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.ivzb.arch.R
import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.Event
import com.ivzb.arch.util.SnackbarMessage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A single source of Snackbar messages related to reservations.
 *
 * Only shows one Snackbar related to one change across all screens
 *
 * Emits new values on request (when a Snackbar is dismissed and ready to show a new message)
 *
 * It keeps a list of [MAX_ITEMS] items, enough to figure out if a message has already been shown,
 * but limited to avoid wasting resources.
 *
 */
@Singleton
open class SnackbarMessageManager @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) {
    companion object {
        // Keep a fixed number of old items
        @VisibleForTesting
        const val MAX_ITEMS = 10
    }

    private val messages = mutableListOf<Event<SnackbarMessage>>()

    private val result = MutableLiveData<Event<SnackbarMessage>>()

    fun addMessage(msg: SnackbarMessage) {
        if (isSnackbarShouldBeIgnored(msg)) {
            return
        }
        // If the new message is about the same change as a pending one, keep the new one. (rare)
        val sameRequestId = messages.filter {
            it.peekContent().requestChangeId == msg.requestChangeId && !it.hasBeenHandled
        }
        if (sameRequestId.isNotEmpty()) {
            messages.removeAll(sameRequestId)
        }

        // If the new message is about a change that was already notified, ignore it.
        val alreadyHandledWithSameId = messages.filter {
            it.peekContent().requestChangeId == msg.requestChangeId && it.hasBeenHandled
        }

        // Only add the message if it hasn't been handled before
        if (alreadyHandledWithSameId.isEmpty()) {
            messages.add(Event(msg))
            loadNextMessage()
        }

        // Remove old messages
        if (messages.size > MAX_ITEMS) {
            messages.retainAll(messages.drop(messages.size - MAX_ITEMS))
        }
    }

    fun loadNextMessage() {
        result.postValue(messages.firstOrNull { !it.hasBeenHandled })
    }

    fun observeNextMessage(): MutableLiveData<Event<SnackbarMessage>> {
        return result
    }

    private fun isSnackbarShouldBeIgnored(msg: SnackbarMessage): Boolean {
        return preferenceStorage.observableSnackbarIsStopped.value == true &&
                msg.actionId == R.string.dont_show
    }
}
