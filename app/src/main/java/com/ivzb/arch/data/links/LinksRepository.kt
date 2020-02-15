package com.ivzb.arch.data.links

import com.ivzb.arch.data.db.AppDatabase
import com.ivzb.arch.data.db.LinkFtsEntity
import com.ivzb.arch.model.Link
import com.ivzb.arch.model.LinkMetaData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to feed data for the presentation layer.
 */
interface LinksRepository {

    fun getAll(): List<Link>

    fun insert(link: Link, linkMetaData: LinkMetaData)

    fun delete(id: Int)
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : LinksRepository {

    override fun getAll(): List<Link> {
        return appDatabase.linksFtsDao().getAll().toSet().map {
            Link(
                id = it.id,
                url = it.url,
                sitename = it.sitename,
                title = it.title,
                imageUrl = it.imageUrl
            )
        }
    }

    override fun insert(link: Link, linkMetaData: LinkMetaData) {
        appDatabase.linksFtsDao().insert(
            LinkFtsEntity(
                id = link.id,
                url = link.url,
                sitename = linkMetaData.sitename,
                title = linkMetaData.title,
                imageUrl = linkMetaData.imageUrl
            )
        )
    }

    override fun delete(linkId: Int) {
        appDatabase.linksFtsDao().delete(linkId)
    }
}
