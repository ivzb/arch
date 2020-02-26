package com.ivzb.arch.ui.add

import androidx.lifecycle.ViewModel
import com.ivzb.arch.di.FragmentScoped
import com.ivzb.arch.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [AddFragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class AddModule {

    /**
     * Generates an AndroidInjector for the [AddFragment].
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAddFragment(): AddFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [AddViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(AddViewModel::class)
    abstract fun bindAddFragmentViewModel(viewModel: AddViewModel): ViewModel
}
