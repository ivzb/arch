package com.ivzb.arch.model

data class Announcement(
    val id: Int,
    val title: String,
    val message: String,
    val imageUrl: String?
) {
    val hasImage = !imageUrl.isNullOrEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Announcement

        if (id != other.id) return false
        if (title != other.title) return false
        if (message != other.message) return false
        if (imageUrl != other.imageUrl) return false
        if (hasImage != other.hasImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + hasImage.hashCode()
        return result
    }


}
