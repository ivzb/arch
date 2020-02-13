package com.ivzb.arch.data.db

import androidx.lifecycle.LiveData
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

    @Query("SELECT rowid, url, sitename, title, imageUrl FROM linkFts ORDER BY rowid DESC")
    fun getAll(): List<LinkFtsEntity>
}
