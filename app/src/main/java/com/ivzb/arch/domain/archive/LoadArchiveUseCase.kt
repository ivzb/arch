package com.ivzb.arch.domain.archive

import com.ivzb.arch.data.archive.ArchiveRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Archive
import javax.inject.Inject

/**
 * Loads all archive items into a list.
 */
open class LoadArchiveUseCase @Inject constructor(
    private val repository: ArchiveRepository
) : UseCase<Unit, List<Archive>>() {

    override fun execute(parameters: Unit): List<Archive> {
        return repository.getAll()
    }
}
