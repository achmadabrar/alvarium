package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class Helpfulness(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("HelpfulNoTotal")
    val helpfulNoTotal: Int?,
    @SerializedName("HelpfulYesTotal")
    val helpfulYesTotal: Int?,
    @SerializedName("ProductReviewId")
    var productReviewId: Long?
)

data class HelpfulnessResponse(
    @SerializedName("Data")
    val data: Helpfulness?
) : BaseResponse()