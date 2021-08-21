package io.github.jisungbin.catviewercompose.api

import io.github.jisungbin.catviewercompose.secret.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatService {
    @Headers("x-api-key: ${ApiConfig.Key}")
    @GET("/v1/images/search?format=json")
    suspend fun requestCats(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ResponseBody>
}
