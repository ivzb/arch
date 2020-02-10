package com.ivzb.arch.domain.feed

import com.ivzb.arch.data.feed.FeedRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Announcement
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

/**
 * Loads all feed items into a list.
 */
open class LoadAnnouncementsUseCase @Inject constructor(
    private val repository: FeedRepository
) : UseCase<Instant, List<Announcement>>() {

    override fun execute(parameters: Instant): List<Announcement> {
        val announcements = repository.getAnnouncements()
        val now = ZonedDateTime.ofInstant(parameters, ZoneId.systemDefault())
        return announcements.filter {
            now.isAfter(it.timestamp)
        }
    }
}
