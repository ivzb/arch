package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

/**
 * Delete link item from db.
 */
open class DeleteLinkUseCase @Inject constructor(
    private val repository: LinksRepository
) : UseCase<Link, Unit>() {

    override fun execute(link: Link) {
        repository.delete(link.id)
    }
}
