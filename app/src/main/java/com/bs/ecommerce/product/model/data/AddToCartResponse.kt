package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class AddToCartResponse(
    @SerializedName("Data") var data: AddToCartData = AddToCartData()
) : BaseResponse()

data class AddToCartData(
    @SerializedName("TotalShoppingCartProducts") var totalShoppingCartProducts: Int = 0,
    @SerializedName("TotalWishListProducts") var totalWishListProducts: Int = 0
)