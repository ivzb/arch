package com.ivzb.arch.util

data class SnackbarMessage(
    /** Resource string ID of the message to show */
    val messageId: Int,

    /** Optional resource string ID for the action (example: "Got it!") */
    val actionId: Int? = null,

    /** Set to true for a Snackbar with long duration  */
    val longDuration: Boolean = false,

    /** Optional change ID to avoid repetition of messages */
    val requestChangeId: String? = null,

    /** Optional message */
    val message: String? = null
) {
    override fun toString(): String {
        return "Message: ${message}. Change: $requestChangeId "
    }
}
