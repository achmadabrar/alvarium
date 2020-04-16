package com.bs.ecommerce.home.homepage.model.data


import com.google.gson.annotations.SerializedName

data class SliderResponse(
    @SerializedName("Data")
    val `data`: SliderData?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)