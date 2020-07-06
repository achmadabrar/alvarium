package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class ReviewOverviewModel(
    @SerializedName("AllowCustomerReviews")
    val allowCustomerReviews: Boolean?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("RatingSum")
    val ratingSum: Int?,
    @SerializedName("TotalReviews")
    val totalReviews: Int?
)