package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinkMetaDataDataSource
import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

/**
 * Insert link item into db.
 */
open class InsertLinkUseCase @Inject constructor(
    private val repository: LinksRepository,
    private val linkMetaDataDataSource: LinkMetaDataDataSource
) : UseCase<Link, Boolean>() {

    override fun execute(link: Link): Boolean {
        val linkMetaData = linkMetaDataDataSource.getLinkMetaData(link.url)

        repository.insert(link, linkMetaData)

        return true
    }
}
