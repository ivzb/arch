package com.ivzb.arch.data.archive

import com.ivzb.arch.data.db.AppDatabase
import com.ivzb.arch.data.db.ArchiveFtsEntity
import com.ivzb.arch.model.Archive
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to feed data for the presentation layer.
 */
interface ArchiveRepository {
    fun getAll(): List<Archive>
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : ArchiveRepository {

    override fun getAll(): List<Archive> {
        // todo: remove insert
        appDatabase.archiveFtsDao().insertAll(listOf(
            ArchiveFtsEntity("1", "title", "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg"),
            ArchiveFtsEntity("2", "title", "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg")
        ))

        return appDatabase.archiveFtsDao().getAll().toSet().map {
            Archive(
                id = it.archiveId,
                title = it.title,
                value = it.value
            )
        }
    }
}
