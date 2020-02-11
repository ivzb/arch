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

    fun insert(archive: Archive)
}

@Singleton
open class DefaultFeedRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : ArchiveRepository {

    override fun getAll(): List<Archive> {
        return appDatabase.archiveFtsDao().getAll().toSet().map {
            Archive(
                id = it.id,
                title = it.title,
                value = it.value
            )
        }
    }

    override fun insert(archive: Archive) {
        appDatabase.archiveFtsDao().insert(
            ArchiveFtsEntity(
                id = archive.id,
                title = archive.title,
                value = archive.value
            )
        )
    }
}
