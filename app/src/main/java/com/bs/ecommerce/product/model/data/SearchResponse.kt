package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Data")
    val `data`: SearchResult?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: String?
)