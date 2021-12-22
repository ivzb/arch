package com.ivzb.arch.data.announcements

import com.ivzb.arch.domain.announcements.AnnouncementsParameters
import com.ivzb.arch.model.Announcement
import javax.inject.Inject
import javax.inject.Singleton

interface AnnouncementsRepository {

    fun getAnnouncements(parameters: AnnouncementsParameters): List<Announcement>
}

@Singleton
open class DefaultAnnouncementsRepository @Inject constructor(
    private val announcementDataSource: AnnouncementDataSource
) : AnnouncementsRepository {

    override fun getAnnouncements(parameters: AnnouncementsParameters): List<Announcement> = announcementDataSource.getAnnouncements(parameters)
}
