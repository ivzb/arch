package com.ivzb.arch.model

data class Announcement(
    val id: Int,
    val title: String,
    val message: String,
    val imageUrl: String?
) {
    val hasImage = !imageUrl.isNullOrEmpty()
}
