package com.ivzb.arch.data.links

import com.ivzb.arch.data.db.AppDatabase
import com.ivzb.arch.data.db.LinkFtsEntity
import com.ivzb.arch.model.Link
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to feed data for the presentation layer.
 */
interface LinksRepository {

    fun getAll(): List<Link>

    fun insert(link: Link)
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : LinksRepository {

    override fun getAll(): List<Link> {
        return appDatabase.linksFtsDao().getAll().toSet().map {
            Link(
                id = it.id,
                title = it.title,
                value = it.value
            )
        }
    }

    override fun insert(link: Link) {
        appDatabase.linksFtsDao().insert(
            LinkFtsEntity(
                id = link.id,
                title = link.title,
                value = link.value
            )
        )
    }
}
