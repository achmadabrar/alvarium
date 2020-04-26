package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class AvailableManufacturer(
    @SerializedName("Disabled")
    val disabled: Boolean?,
    @SerializedName("Group")
    val group: Any?,
    @SerializedName("Selected")
    val selected: Boolean?,
    @SerializedName("Text")
    val text: String?,
    @SerializedName("Value")
    val value: String?
)