package com.bs.ecommerce.account.review.model.data


import com.bs.ecommerce.account.review.model.data.MyReviewData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class MyReviewsResponse(
    @SerializedName("Data")
    val `data`: MyReviewData?
): BaseResponse()