package io.github.jisungbin.catviewercompose.api

import io.github.jisungbin.catviewercompose.model.Cat
import retrofit2.Response
import retrofit2.http.GET

interface CatService {
    @GET("/cat?json=true")
    suspend fun requestNewCat(): Response<Cat>
}
