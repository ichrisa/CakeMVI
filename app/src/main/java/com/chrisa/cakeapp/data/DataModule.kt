package com.chrisa.cakeapp.data

import com.chrisa.cakeapp.data.api.ApiConfig
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        ApiModule::class
    ]
)
class DataModule(
    private val apiConfig: ApiConfig
) {
    @Provides
    fun apiConfig(): ApiConfig {
        return apiConfig
    }
}