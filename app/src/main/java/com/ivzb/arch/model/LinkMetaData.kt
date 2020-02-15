package com.ivzb.arch.model

data class LinkMetaData(
    var url: String,
    var sitename: String? = null,
    var imageUrl: String? = null,
    var title: String? = null,
    var description: String? = null
)
