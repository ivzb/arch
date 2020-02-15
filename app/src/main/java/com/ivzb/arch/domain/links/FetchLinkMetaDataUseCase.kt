package com.ivzb.arch.domain.links

import com.ivzb.arch.data.links.LinkMetaDataDataSource
import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Link
import javax.inject.Inject

/**
 * Update link item into db.
 */
open class FetchLinkMetaDataUseCase @Inject constructor(
    private val repository: LinksRepository,
    private val linkMetaDataDataSource: LinkMetaDataDataSource
) : UseCase<Link, Unit>() {

    override fun execute(link: Link) {
        val linkMetaData = linkMetaDataDataSource.getLinkMetaData(link.url)

        repository.update(linkMetaData)
    }
}
