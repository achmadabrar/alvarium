package com.bs.ecommerce.home.homepage.model.data


import com.google.gson.annotations.SerializedName

data class SliderData(
    @SerializedName("IsEnabled")
    val isEnabled: Boolean? = false,
    @SerializedName("Sliders")
    val sliders: List<Slider>? = listOf()
)