package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class AddProductReview(
    @SerializedName("CanCurrentCustomerLeaveReview")
    var canCurrentCustomerLeaveReview: Boolean?,
    @SerializedName("CustomProperties")
    var customProperties: CustomProperties?,
    @SerializedName("DisplayCaptcha")
    var displayCaptcha: Boolean?,
    @SerializedName("Rating")
    var rating: Int?,
    @SerializedName("Result")
    var result: String?,
    @SerializedName("ReviewText")
    var reviewText: String?,
    @SerializedName("SuccessfullyAdded")
    var successfullyAdded: Boolean?,
    @SerializedName("Title")
    var title: String?
)