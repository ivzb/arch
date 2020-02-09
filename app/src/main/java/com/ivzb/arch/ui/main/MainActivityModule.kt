package com.ivzb.arch.ui.main

import androidx.lifecycle.ViewModel
import com.ivzb.arch.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainActivityViewModel): ViewModel
}
