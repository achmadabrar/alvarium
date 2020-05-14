package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ProductReviewData(
    @SerializedName("AddAdditionalProductReviewList")
    val addAdditionalProductReviewList: List<Any>?,
    @SerializedName("AddProductReview")
    val addProductReview: AddProductReview?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Items")
    val items: List<ProductReviewItem>?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("ReviewTypeList")
    val reviewTypeList: List<Any>?
)