package io.github.jisungbin.catviewercompose.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jisungbin.catviewercompose.secret.ApiConfig
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatApiModule {
    @Provides
    @Singleton
    fun provideCatApi(): CatService = Retrofit.Builder()
        .baseUrl(ApiConfig.BaseUrl)
        .build()
        .create(CatService::class.java)
}
