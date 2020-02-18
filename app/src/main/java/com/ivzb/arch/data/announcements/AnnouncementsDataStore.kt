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
            title = "Save your first like",
            imageUrl = null,
            message = "Click share..."
        )
    )
}
