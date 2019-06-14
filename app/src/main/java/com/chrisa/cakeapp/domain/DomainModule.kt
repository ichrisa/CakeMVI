package com.chrisa.cakeapp.domain

import com.chrisa.cakeapp.domain.cakes.CakesDomainModule
import dagger.Module

@Module(
    includes = [
        CakesDomainModule::class
    ]
)
internal object DomainModule
