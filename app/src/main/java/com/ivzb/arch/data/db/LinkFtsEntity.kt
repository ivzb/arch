package com.ivzb.arch.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * This class represents [Link] data for searching with FTS.
 *
 * The [ColumnInfo] name is explicitly declared to allow flexibility for renaming the data class
 * properties without requiring changing the column name.
 */
@Entity(tableName = "linkFts")
@Fts4
data class LinkFtsEntity(

    /**
     * An FTS entity table always has a column named rowid that is the equivalent of an
     * INTEGER PRIMARY KEY index. Therefore, an FTS entity can only have a single field
     * annotated with PrimaryKey, it must be named rowid and must be of INTEGER affinity.
     *
     * The field can be optionally omitted in the class (as is done here),
     * but can still be used in queries.
     */

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val id: Int = 0,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "sitename")
    val sitename: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = null
)
