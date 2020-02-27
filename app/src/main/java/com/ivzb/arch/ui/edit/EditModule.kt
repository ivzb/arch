package com.ivzb.arch.ui.edit

import androidx.lifecycle.ViewModel
import com.ivzb.arch.di.FragmentScoped
import com.ivzb.arch.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [Editragment] are defined.
 */
@Module
@Suppress("UNUSED")
internal abstract class EditModule {

    /**
     * Generates an AndroidInjector for the [EditFragment].
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEditFragment(): EditFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [EditViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(EditViewModel::class)
    abstract fun bindEditFragmentViewModel(viewModel: EditViewModel): ViewModel
}
