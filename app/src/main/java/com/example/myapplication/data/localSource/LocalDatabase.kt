package com.example.myapplication.data.localSource

import android.content.SharedPreferences

class LocalDatabase(private val sPref: SharedPreferences) {

    fun addFavourite(code: String) {
        sPref.edit().putBoolean(code, true).apply()
    }

    fun delete(code: String) {
        sPref.edit().remove(code).apply()
    }

    fun isFavorite(code: String): Boolean {
        return sPref.contains(code)
    }

}