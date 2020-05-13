package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class AddProductReview(
    @SerializedName("CanCurrentCustomerLeaveReview")
    val canCurrentCustomerLeaveReview: Boolean?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DisplayCaptcha")
    val displayCaptcha: Boolean?,
    @SerializedName("Rating")
    val rating: Int?,
    @SerializedName("Result")
    val result: Any?,
    @SerializedName("ReviewText")
    val reviewText: String?,
    @SerializedName("SuccessfullyAdded")
    val successfullyAdded: Boolean?,
    @SerializedName("Title")
    val title: String?
)