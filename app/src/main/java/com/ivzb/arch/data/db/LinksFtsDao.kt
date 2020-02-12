package com.ivzb.arch.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The Data Access Object for the [LinkFtsEntity] class.
 */
@Dao
interface LinksFtsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(link: LinkFtsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(link: List<LinkFtsEntity>)

    @Query("SELECT rowid, title, value FROM linkFts")
    fun getAll(): List<LinkFtsEntity>
}
