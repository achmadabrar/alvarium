package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class WishListResponse(
    @SerializedName("Data")
    val wishListData: WishListData?
) : BaseResponse()