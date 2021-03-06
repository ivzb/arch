package com.ivzb.arch.util

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.widget.FadingSnackbar

/**
 * An extension for Fragments that sets up a Snackbar with a [SnackbarMessageManager].
 */
fun Fragment.setUpSnackbar(
    snackbarMessage: LiveData<Event<SnackbarMessage>>,
    fadingSnackbar: FadingSnackbar,
    snackbarMessageManager: SnackbarMessageManager,
    actionClickListener: () -> Unit = {}
) {
    // Show messages generated by the ViewModel
    snackbarMessage.observe(this, EventObserver { message: SnackbarMessage ->
        fadingSnackbar.show(
            messageId = message.messageId,
            actionId = message.actionId,
            longDuration = message.longDuration,
            actionClick = {
                actionClickListener()
                fadingSnackbar.dismiss()
            }
        )
    })

    // Important reservations messages are handled with a message manager
    snackbarMessageManager.observeNextMessage().observe(this, EventObserver { message ->
        val messageText = HtmlCompat.fromHtml(
            requireContext().getString(message.messageId, message.message),
            FROM_HTML_MODE_LEGACY
        )
        fadingSnackbar.show(
            messageText = messageText,
            actionId = message.actionId,
            longDuration = message.longDuration,
            actionClick = {
                actionClickListener()
                fadingSnackbar.dismiss()
            },
            // When the snackbar is dismissed, ping the snackbar message manager in case there
            // are pending messages.
            dismissListener = { snackbarMessageManager.loadNextMessage() }
        )
    })
}
