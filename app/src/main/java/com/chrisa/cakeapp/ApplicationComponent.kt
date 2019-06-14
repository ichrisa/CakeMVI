package com.chrisa.cakeapp

import android.content.Context
import com.chrisa.cakeapp.data.DataModule
import com.chrisa.cakeapp.domain.DomainModule
import com.chrisa.cakeapp.presentation.PresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        DomainModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<CakeApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun dataModule(dataModule: DataModule): Builder

        fun build(): ApplicationComponent
    }
}
