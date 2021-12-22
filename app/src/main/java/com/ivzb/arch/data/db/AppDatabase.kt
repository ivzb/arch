package com.ivzb.arch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


/**
 * The [Room] database for this app.
 */
@Database(
    entities = [
        LinkFtsEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun linksFtsDao(): LinksFtsDao

    companion object {
        private const val databaseName = "arch-db"

        fun buildDatabase(context: Context): AppDatabase {
            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE VIRTUAL TABLE tempLinkFts USING FTS3(url TEXT NOT NULL, sitename TEXT, title TEXT, imageUrl TEXT);")
                database.execSQL("INSERT INTO tempLinkFts (url, sitename, title, imageUrl) SELECT url, sitename, title, imageUrl FROM linkFts;")
                database.execSQL("DROP TABLE linkFts;")
                database.execSQL( "CREATE VIRTUAL TABLE linkFts USING FTS3(url TEXT NOT NULL, category TEXT NOT NULL, sitename TEXT, title TEXT, imageUrl TEXT);")
                database.execSQL("INSERT INTO linkFts (url, category, sitename, title, imageUrl) SELECT url, '', sitename, title, imageUrl FROM tempLinkFts;")
                database.execSQL("DROP TABLE tempLinkFts;")
            }
        }
    }
}
