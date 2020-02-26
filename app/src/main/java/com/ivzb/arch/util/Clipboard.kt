package com.ivzb.arch.util

import android.content.ClipDescription
import android.content.ClipboardManager

fun ClipboardManager.hasLink() = when {
    !hasPrimaryClip() -> {
        // disable add link button, since the clipboard is empty
        false
    }

    !(primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ?: false) -> {
        // disable add link button, since the clipboard has data but it is not plain text
        false
    }

    (primaryClip?.itemCount ?: 0 > 0) && (primaryClip?.getItemAt(0)?.text?.trim()?.isEmpty()
        ?: false) -> {
        // disable add link button, since the clipboard is empty
        false
    }

    else -> {
        // enable add link button, since the clipboard contains plain text
        true
    }
}

fun ClipboardManager.getLink() = primaryClip?.getItemAt(0)?.text.toString()
