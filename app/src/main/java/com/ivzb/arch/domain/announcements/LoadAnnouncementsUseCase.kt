package com.ivzb.arch.domain.announcements

import com.ivzb.arch.data.announcements.AnnouncementsRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Announcement
import javax.inject.Inject

open class LoadAnnouncementsUseCase @Inject constructor(
    private val repository: AnnouncementsRepository
) : UseCase<Unit, List<Announcement>>() {

    override fun execute(parameters: Unit): List<Announcement> {
        return repository.getAnnouncements()
    }
}
