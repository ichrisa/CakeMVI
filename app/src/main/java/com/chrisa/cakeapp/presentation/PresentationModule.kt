package com.chrisa.cakeapp.presentation

import com.chrisa.cakeapp.presentation.cakes.CakesFragment
import com.chrisa.cakeapp.presentation.cakes.CakesFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PresentationModule {
    @ContributesAndroidInjector(modules = [CakesFragmentModule::class])
    internal abstract fun contributeCakesFragment(): CakesFragment
}