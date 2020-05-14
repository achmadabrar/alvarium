package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ProductReview(
    @SerializedName("AdditionalProductReviewList")
    val additionalProductReviewList: List<Any>?,
    @SerializedName("ApprovalStatus")
    val approvalStatus: Any?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("ProductId")
    val productId: Long?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("Rating")
    val rating: Int?,
    @SerializedName("ReplyText")
    val replyText: Any?,
    @SerializedName("ReviewText")
    val reviewText: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("WrittenOnStr")
    val writtenOnStr: String?
)