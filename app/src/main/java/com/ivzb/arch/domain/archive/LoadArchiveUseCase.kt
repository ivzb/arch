package com.ivzb.arch.domain.archive

import com.ivzb.arch.data.archive.ArchiveRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Archive
import org.threeten.bp.Instant
import javax.inject.Inject

/**
 * Loads all archive items into a list.
 */
open class LoadArchiveUseCase @Inject constructor(
    private val repository: ArchiveRepository
) : UseCase<Instant, List<Archive>>() {

    override fun execute(parameters: Instant): List<Archive> {
        return repository.getAll()
    }
}
