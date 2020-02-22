package com.ivzb.arch.ui.search

import androidx.lifecycle.ViewModel
import com.ivzb.arch.di.FragmentScoped
import com.ivzb.arch.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [SearchFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class SearchModule {

    /**
     * Generates an [AndroidInjector] for the [SearchFragment]
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragment(): SearchFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [SearchViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewmodel: SearchViewModel): ViewModel
}
