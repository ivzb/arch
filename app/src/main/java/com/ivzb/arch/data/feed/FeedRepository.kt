package com.ivzb.arch.data.feed

import com.ivzb.arch.model.Announcement
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to feed data for the presentation layer.
 */
interface FeedRepository {
    fun getAnnouncements(): List<Announcement>
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val announcementDataSource: AnnouncementDataSource
) : FeedRepository {

    override fun getAnnouncements(): List<Announcement> = announcementDataSource.getAnnouncements()
}
