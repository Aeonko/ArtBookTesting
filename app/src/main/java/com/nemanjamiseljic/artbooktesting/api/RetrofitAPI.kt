package com.nemanjamiseljic.artbooktesting.api

import com.nemanjamiseljic.artbooktesting.model.ImageResponse
import com.nemanjamiseljic.artbooktesting.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/api/")
    suspend fun imagesSearch(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = API_KEY
    ): Response<ImageResponse>
}