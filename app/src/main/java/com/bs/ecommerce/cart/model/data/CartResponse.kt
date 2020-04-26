package com.bs.ecommerce.cart.model.data
import com.google.gson.annotations.SerializedName





data class CartRootData(
    @SerializedName("Cart") var cart: CartInfoData = CartInfoData(),
    @SerializedName("OrderTotals") var orderTotals: OrderTotal = OrderTotal()
)


data class CartResponse(
    @SerializedName("Data") var cartRootData: CartRootData = CartRootData(),
    @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
    @SerializedName("Message") var message: Any = Any()
)





