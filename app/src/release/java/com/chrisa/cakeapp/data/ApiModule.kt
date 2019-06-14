package com.chrisa.cakeapp.data

import com.chrisa.cakeapp.data.api.ApiConfig
import com.chrisa.cakeapp.data.api.cakes.CakeApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
internal object ApiModule {

    @JvmStatic
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }

    @JvmStatic
    @Provides
    fun retrofit(
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.apiEndpoint)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @JvmStatic
    @Provides
    fun cakeApi(retrofit: Retrofit): CakeApi {
        return retrofit.create(CakeApi::class.java)
    }
}
