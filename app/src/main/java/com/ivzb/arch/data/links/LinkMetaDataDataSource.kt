package com.ivzb.arch.data.links

import android.content.Context
import com.ivzb.arch.model.LinkMetaData
import com.ivzb.arch.util.NetworkUtils
import java.io.IOException
import javax.inject.Inject

/**
 * Downloads and parses link metadata.
 */
class LinkMetaDataDataSource @Inject constructor(
    val context: Context,
    private val networkUtils: NetworkUtils
) {

    fun getLinkMetaData(url: String): LinkMetaData {
        if (!networkUtils.hasNetworkConnection()) {
            return LinkMetaData(
                url = url
            )
        }

        return try {
            LinkMetaDataJsonParser.fetch(url)
        } catch (e: IOException) {
            return LinkMetaData(
                url = url
            )
        }
    }
}
