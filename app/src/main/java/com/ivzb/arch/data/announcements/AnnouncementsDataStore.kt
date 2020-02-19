package com.ivzb.arch.data.announcements

import com.ivzb.arch.model.Announcement
import javax.inject.Inject

interface AnnouncementDataSource {

    fun getAnnouncements(): List<Announcement>
}

class DefaultAnnouncementDataSource @Inject constructor() : AnnouncementDataSource {

    override fun getAnnouncements() = listOf(
        Announcement(
            id = 1,
            title = "Save your first link",
            imageUrl = null,
            message = "Paste link from clipboard or share to Arch app."
        )
    )
}
