package com.ivzb.arch.model

/**
 * Describes an item in the feed, displaying social-media like updates.
 * Each item includes a message, id, a title and a value.
 */
data class Link(
    val id: Int = 0,
    val url: String,
    val sitename: String? = null,
    val title: String? = null,
    val imageUrl: String? = null
)
