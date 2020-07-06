package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class RouteValues(
    @SerializedName("pageNumber")
    val pageNumber: Int?
)