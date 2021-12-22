package com.ivzb.arch.data.announcements

import com.ivzb.arch.domain.announcements.AnnouncementsParameters
import com.ivzb.arch.model.Announcement
import javax.inject.Inject

interface AnnouncementDataSource {

    fun getAnnouncements(parameters: AnnouncementsParameters): List<Announcement>
}

class DefaultAnnouncementDataSource @Inject constructor() : AnnouncementDataSource {

    override fun getAnnouncements(parameters: AnnouncementsParameters): List<Announcement> {
        val announcements = mutableListOf<Announcement>()

        if (!parameters.hasLinks) {
            announcements.add(
                Announcement(
                    id = 1,
                    title = "Save your first link or note.",
                    imageUrl = null,
                    message = "Paste it from clipboard or share to Arch app."
                )
            )
        }

        if (parameters.hasLinks && !parameters.hasCategories) {
            announcements.add(
                Announcement(
                    id = 1,
                    title = "Categories are now available.",
                    imageUrl = null,
                    message = "Save your next link or note with category or edit existing one."
                )
            )
        }

        return announcements
    }
}
