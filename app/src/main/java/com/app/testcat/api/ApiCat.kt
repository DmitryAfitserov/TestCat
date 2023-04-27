package com.app.testcat.api

import com.app.testcat.model.CatNet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCat {

    @GET("images/search?")
    suspend fun getAllCats(@Query("limit") limit: Int ): Response<List<CatNet>>

}