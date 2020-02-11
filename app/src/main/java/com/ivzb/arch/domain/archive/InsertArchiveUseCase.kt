package com.ivzb.arch.domain.archive

import com.ivzb.arch.data.archive.ArchiveRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Archive
import javax.inject.Inject

/**
 * Insert archive item into db.
 */
open class InsertArchiveUseCase @Inject constructor(
    private val repository: ArchiveRepository
) : UseCase<Archive, Unit>() {

    override fun execute(parameters: Archive) {
        return repository.insert(parameters)
    }
}
