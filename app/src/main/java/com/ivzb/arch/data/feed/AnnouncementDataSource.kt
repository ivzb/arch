package com.ivzb.arch.data.feed

import com.ivzb.arch.model.Announcement
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

interface AnnouncementDataSource {
    fun getAnnouncements(): List<Announcement>
}

class MockAnnouncementDataSource @Inject constructor() : AnnouncementDataSource {

    override fun getAnnouncements(): List<Announcement> {
        return listOf(
            Announcement("1", "title", "message", true, true, ZonedDateTime.now().minusHours(1), "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg", "category", 0),
            Announcement("2", "title", "message", false, false, ZonedDateTime.now().minusHours(2), "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg", "category", 0)
        )
    }
}
