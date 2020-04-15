package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

data class AvailableSortOption(
    @SerializedName("Disabled")
    val disabled: Boolean? = false,
    @SerializedName("Selected")
    val selected: Boolean? = false,
    @SerializedName("Text")
    val text: String? = "",
    @SerializedName("Value")
    val value: String? = ""
)