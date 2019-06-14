package com.chrisa.cakeapp

import com.chrisa.cakeapp.data.api.ApiConfig
import com.chrisa.cakeapp.data.DataModule

object Config {

    private fun apiConfig(): ApiConfig {
        return ApiConfig("https://gist.githubusercontent.com")
    }

    fun applicationInjector(app: CakeApp): ApplicationComponent {

        return DaggerApplicationComponent.builder()
            .context(app)
            .dataModule(DataModule(apiConfig()))
            .build()
    }
}