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
    val imageUrl: String? = null,
    val category: String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Link

        if (id != other.id) return false
        if (url != other.url) return false
        if (sitename != other.sitename) return false
        if (title != other.title) return false
        if (imageUrl != other.imageUrl) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + url.hashCode()
        result = 31 * result + (sitename?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (category.hashCode() ?: 0)
        return result
    }
}
