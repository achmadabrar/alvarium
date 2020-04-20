package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class PageSizeOption(
    @SerializedName("Disabled")
    val disabled: Boolean? = false,
    @SerializedName("Selected")
    val selected: Boolean? = false,
    @SerializedName("Text")
    val text: String? = "",
    @SerializedName("Value")
    val value: String? = ""
)