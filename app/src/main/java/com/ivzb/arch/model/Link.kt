package com.ivzb.arch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Describes an item in the feed, displaying social-media like updates.
 * Each item includes a message, id, a title and a value.
 */
@Parcelize
data class Link(
    val id: Int = 0,
    val url: String,
    val sitename: String? = null,
    val title: String? = null,
    val imageUrl: String? = null
) : Parcelable
