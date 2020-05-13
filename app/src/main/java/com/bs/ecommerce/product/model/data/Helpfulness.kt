package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class Helpfulness(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("HelpfulNoTotal")
    val helpfulNoTotal: Int?,
    @SerializedName("HelpfulYesTotal")
    val helpfulYesTotal: Int?,
    @SerializedName("ProductReviewId")
    val productReviewId: Int?
)