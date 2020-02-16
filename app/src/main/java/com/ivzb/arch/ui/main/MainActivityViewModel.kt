package com.ivzb.arch.ui.main

import android.content.Intent
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.links.FetchLinkMetaDataUseCase
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.theme.ThemedActivityDelegate
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    themedActivityDelegate: ThemedActivityDelegate,
    val insertLinkUseCase: InsertLinkUseCase,
    val fetchLinkMetaDataUseCase: FetchLinkMetaDataUseCase
) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {

    fun handleLink(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Link(url = extractUrl(it)).let {
                insertLinkUseCase(it)
                fetchLinkMetaDataUseCase(it)
            }
        }
    }

    private fun extractUrl(input: String) =
        input
            .split(" ")
            .firstOrNull { Patterns.WEB_URL.matcher(it).find() }
            ?: input
}
