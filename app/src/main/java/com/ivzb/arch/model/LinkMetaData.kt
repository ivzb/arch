package com.ivzb.arch.model

data class LinkMetaData(
    var url: String,
    var sitename: String? = null,
    var imageUrl: String? = null,
    var title: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LinkMetaData

        if (url != other.url) return false
        if (sitename != other.sitename) return false
        if (imageUrl != other.imageUrl) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + (sitename?.hashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }
}
