package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class WishListResponse(
    @SerializedName("Data")
    val wishListData: WishListData?,
    @SerializedName("ErrorList")
    val errorList: List<Any>?,
    @SerializedName("Message")
    val message: Any?
)