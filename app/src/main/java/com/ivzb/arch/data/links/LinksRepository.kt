package com.ivzb.arch.data.links

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    fun observeAll(): LiveData<List<Link>>

    fun insert(link: Link)

    fun update(linkMetaData: LinkMetaData)

    fun delete(id: Int)
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : LinksRepository {

    override fun getAll(): List<Link> {
        return appDatabase.linksFtsDao().getAll().toSet().map {
            mapLink(it)
        }
    }

    override fun observeAll(): LiveData<List<Link>> {
        return Transformations.map(
            appDatabase.linksFtsDao().observeAll()
        ) {
            it
                .toSet()
                .map {
                    mapLink(it)
                }
        }
    }

    override fun insert(link: Link) {
        appDatabase.linksFtsDao().insert(
            LinkFtsEntity(
                id = link.id,
                url = link.url
            )
        )
    }

    override fun update(linkMetaData: LinkMetaData) {
        appDatabase.linksFtsDao().update(
            linkMetaData.url,
            linkMetaData.sitename,
            linkMetaData.title,
            linkMetaData.imageUrl
        )
    }

    override fun delete(linkId: Int) {
        appDatabase.linksFtsDao().delete(linkId)
    }

    private fun mapLink(it: LinkFtsEntity) = Link(
        id = it.id,
        url = it.url,
        sitename = it.sitename,
        title = it.title,
        imageUrl = it.imageUrl
    )
}
