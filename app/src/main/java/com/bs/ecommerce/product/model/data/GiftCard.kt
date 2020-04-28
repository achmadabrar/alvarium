package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class GiftCard(
    @SerializedName("Amount")
    val amount: String?,
    @SerializedName("CouponCode")
    val couponCode: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
)