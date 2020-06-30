package com.bs.ecommerce.account.review.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.PagerModel
import com.google.gson.annotations.SerializedName

data class MyReviewData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("PagerModel")
    val pagerModel: PagerModel?,
    @SerializedName("ProductReviews")
    val productReviews: List<ProductReview>?
)