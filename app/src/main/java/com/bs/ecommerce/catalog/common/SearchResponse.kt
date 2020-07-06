package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Data")
    val `data`: SearchResult?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: String?
)