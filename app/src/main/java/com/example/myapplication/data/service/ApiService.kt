package com.example.myapplication.data.service

import com.example.myapplication.data.entity.SmartphoneDataResponse
import com.example.myapplication.data.entity.SmartphoneDetailDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("catalog")
    suspend fun getSmartphones(
        @Query("page") page: Int,
        @Query("page_limit") limit: Int,
        @Query("section") section: String
    ): Response<SmartphoneDataResponse>

    @GET("product/{id}")
    suspend fun getSmartphoneDetail(@Path("id") id: String): Response<SmartphoneDetailDataResponse>
}