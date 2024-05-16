package com.example.myapplication.data.entity

import com.google.gson.annotations.SerializedName

data class SmartphoneDetailDataResponse(
    val data: SmartphoneDetails
) {
    data class SmartphoneDetails(
        val id: Int,
        val code: String,
        val name: String,
        val title: String,
        val photos: List<String>,
        val inFavorites: Boolean = false,
        @SerializedName("main_properties")
        val mainProperties: List<MainProperties>
    )

    data class MainProperties(
        @SerializedName("prop_name")
        val propName: String,
        @SerializedName("prop_value")
        val propValue: String
    )
}
