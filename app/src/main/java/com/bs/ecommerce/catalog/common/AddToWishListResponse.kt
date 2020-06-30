package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class AddToWishListResponse(
    @SerializedName("Data")
    val redirectionModel: RedirectionModel?
): BaseResponse()