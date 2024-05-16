package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.SmartphoneDataResponse
import com.example.myapplication.data.entity.SmartphoneDetailDataResponse
import com.example.myapplication.data.localSource.LocalDatabase
import com.example.myapplication.data.service.ApiService

class SmartphoneRepository(
    private val apiService: ApiService,
    private val localStorage: LocalDatabase
) {
    suspend fun getSmartphones(page: Int, limit: Int): SmartphoneDataResponse? {
        val response = apiService.getSmartphones(page, limit, "smartfony")
        return response.body()
    }

    suspend fun getSmartphoneDetail(id: String): SmartphoneDetailDataResponse? {
        val response = apiService.getSmartphoneDetail(id)
        return response.body()
    }

    suspend fun addFavorite(code: String) {
        localStorage.addFavourite(code)
    }

    suspend fun removeFavorite(code: String) {
        localStorage.delete(code)
    }

    fun isFavorite(code: String): Boolean {
        return localStorage.isFavorite(code)
    }
}
