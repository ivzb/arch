package com.ivzb.arch.ui.theme

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Suppress("UNUSED")
abstract class ThemedActivityDelegateModule {

    @Singleton
    @Binds
    abstract fun provideThemedActivityDelegate(
        impl: ThemedActivityDelegateImpl
    ): ThemedActivityDelegate
}
