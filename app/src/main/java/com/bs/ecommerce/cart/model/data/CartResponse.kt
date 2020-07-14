package com.bs.ecommerce.cart.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class CartRootData(
    @SerializedName("Cart") var cart: CartInfoData = CartInfoData(),
    @SerializedName("OrderTotals") var orderTotals: OrderTotal = OrderTotal(),
    @SerializedName("EstimateShipping") var estimateShipping: EstimateShipping = EstimateShipping()
)

open class CartResponse(@SerializedName("Data") var cartRootData: CartRootData = CartRootData()) :
    BaseResponse()





