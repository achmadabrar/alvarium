package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("Data")
    val `data`: CategoryModel? = CategoryModel(),
    @SerializedName("ErrorList")
    val errorList: List<Any>? = listOf()
)