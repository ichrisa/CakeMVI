package com.chrisa.cakeapp.domain.cakes

import dagger.Binds
import dagger.Module

@Module
internal abstract class CakesDomainModule {
    @Binds
    internal abstract fun cakesUseCase(cakesUseCaseImpl: CakesUseCaseImpl): CakesUseCase
}
