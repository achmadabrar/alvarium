package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class MyReviewData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("PagerModel")
    val pagerModel: PagerModel?,
    @SerializedName("ProductReviews")
    val productReviews: List<ProductReview>?
)