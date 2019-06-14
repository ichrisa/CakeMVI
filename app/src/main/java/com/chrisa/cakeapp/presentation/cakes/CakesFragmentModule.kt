package com.chrisa.cakeapp.presentation.cakes

import androidx.lifecycle.ViewModel
import com.chrisa.core.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CakesFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(CakesViewModel::class)
    internal abstract fun cakesViewModel(viewModel: CakesViewModel): ViewModel
}
