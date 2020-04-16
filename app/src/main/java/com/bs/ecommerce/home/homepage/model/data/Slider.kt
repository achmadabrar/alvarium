package com.bs.ecommerce.home.homepage.model.data


import com.google.gson.annotations.SerializedName

data class Slider(
    @SerializedName("EntityId")
    val entityId: Int?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("ImageUrl")
    val imageUrl: String?,
    @SerializedName("SliderType")
    val sliderType: Int?
)