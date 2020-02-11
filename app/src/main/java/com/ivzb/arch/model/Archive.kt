package com.ivzb.arch.model

/**
 * Describes an item in the feed, displaying social-media like updates.
 * Each item includes a message, id, a title and a value.
 */
data class Archive(
    val id: Int = 0,
    val title: String,
    val value: String,
    val hasImage: Boolean = true,
    val imageUrl: String = "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg"
)
