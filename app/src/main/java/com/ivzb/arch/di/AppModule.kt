package com.ivzb.arch.di

import android.content.Context
import com.ivzb.arch.MainApplication
import com.ivzb.arch.data.archive.*
import com.ivzb.arch.data.db.AppDatabase
import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.data.prefs.SharedPreferenceStorage
import com.ivzb.arch.domain.time.DefaultTimeProvider
import com.ivzb.arch.domain.time.TimeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@Module
class AppModule {

    @Provides
    fun provideContext(application: MainApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider {
        return DefaultTimeProvider
    }

    @Singleton
    @Provides
    fun provideArchiveRepository(appDatabase: AppDatabase): ArchiveRepository {
        return DefaultFeedRepository(appDatabase)
    }

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context): AppDatabase = AppDatabase.buildDatabase(context)
}
