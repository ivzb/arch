package com.ivzb.arch.di

import android.content.Context
import com.ivzb.arch.data.announcements.AnnouncementDataSource
import com.ivzb.arch.data.announcements.AnnouncementsRepository
import com.ivzb.arch.data.announcements.DefaultAnnouncementDataSource
import com.ivzb.arch.data.announcements.DefaultAnnouncementsRepository
import com.ivzb.arch.data.db.AppDatabase
import com.ivzb.arch.data.links.DefaultFeedRepository
import com.ivzb.arch.data.links.LinkMetaDataDataSource
import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.util.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context): AppDatabase = AppDatabase.buildDatabase(context)

    @Singleton
    @Provides
    fun provideLinkMetaDataDataSource(
        context: Context,
        networkUtils: NetworkUtils
    ): LinkMetaDataDataSource = LinkMetaDataDataSource(context, networkUtils)

    @Singleton
    @Provides
    fun provideLinksRepository(
        appDatabase: AppDatabase
    ): LinksRepository = DefaultFeedRepository(appDatabase)

    @Singleton
    @Provides
    fun provideAnnouncementDataSource(): AnnouncementDataSource {
        return DefaultAnnouncementDataSource()
    }

    @Singleton
    @Provides
    fun provideAnnouncementsRepository(
        announcementDataSource: AnnouncementDataSource
    ): AnnouncementsRepository {
        return DefaultAnnouncementsRepository(announcementDataSource)
    }
}
