package com.ivzb.arch.ui.feed

import androidx.lifecycle.ViewModel
import com.ivzb.arch.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [FeedFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class FeedModule {

    /**
     * Generates an [AndroidInjector] for the [FeedFragment] as a Dagger subcomponent of the
     * [MainModule].
     */
    @ContributesAndroidInjector
    internal abstract fun provideFeedFragment(): FeedFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [FeedViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindFeedFragmentViewModel(viewModel: FeedViewModel): ViewModel
}
