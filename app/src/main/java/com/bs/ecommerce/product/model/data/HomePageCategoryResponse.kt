package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

data class HomePageCategoryResponse (
    @SerializedName("Data")
    val categoryList: List<CategoryModel>? = listOf(),
    @SerializedName("ErrorList")
    val errorList: List<String>? = listOf(),
    @SerializedName("Message")
    val message: Any?
)