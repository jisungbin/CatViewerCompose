package io.github.jisungbin.catviewercompose.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CatClient {
    const val BaseUrl = "https://cataas.com"

    val get: CatService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatService::class.java)
    }
}
