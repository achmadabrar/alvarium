package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class ProductReviewResponse(
    @SerializedName("Data")
    val `data`: ProductReviewData?
): BaseResponse()