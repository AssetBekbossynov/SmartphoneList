package com.example.myapplication.data.entity

import com.google.gson.annotations.SerializedName

data class SmartphoneDataResponse(
    val data: SmartphoneListData
) {

    data class SmartphoneListData(
        @SerializedName("page_items_count")
        val pageItemsCount: Int,

        @SerializedName("all_items_count")
        val allItemsCount: Int,

        val items: List<Smartphone>
    )

    data class Smartphone(
        val id: Int,
        val name: String,
        val title: String,
        val code: String,
        val photos: List<String>,
        @SerializedName("in_favorites")
        val inFavorites: Boolean
    )
}
