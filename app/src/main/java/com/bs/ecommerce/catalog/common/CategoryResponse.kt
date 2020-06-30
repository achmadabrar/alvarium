package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CategoryModel
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("Data")
    val `data`: CategoryModel? = CategoryModel(),
    @SerializedName("ErrorList")
    val errorList: List<Any>? = listOf()
)