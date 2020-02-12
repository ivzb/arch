package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

/**
 * Insert link item into db.
 */
open class InsertLinkUseCase @Inject constructor(
    private val repository: LinksRepository
) : UseCase<Link, Unit>() {

    override fun execute(parameters: Link) {
        return repository.insert(parameters)
    }
}
