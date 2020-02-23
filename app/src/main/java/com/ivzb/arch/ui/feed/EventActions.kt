package com.ivzb.arch.ui.feed

import com.ivzb.arch.model.Link

interface EventActions {

    fun click(link: Link)

    fun longClick(link: Link)
}
