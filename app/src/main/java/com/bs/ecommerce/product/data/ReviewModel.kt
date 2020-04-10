package com.bs.ecommerce.product.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/11/2015.
 */
class ReviewModel {
    @SerializedName("ProductId") var productId: Long = 0

    @SerializedName("RatingSum") var ratingSum: Long = 0
    @SerializedName("IsAllowCustomerReviews") var isAllowCustomerReviews: Boolean = false
    @SerializedName("TotalReviews") var totalReviews: Long = 0
}
