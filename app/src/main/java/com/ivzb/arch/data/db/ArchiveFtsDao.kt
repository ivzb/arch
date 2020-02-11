package com.ivzb.arch.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The Data Access Object for the [ArchiveFtsEntity] class.
 */
@Dao
interface ArchiveFtsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(archive: List<ArchiveFtsEntity>)

    @Query("SELECT * FROM archiveFts")
    fun getAll(): List<ArchiveFtsEntity>
}
