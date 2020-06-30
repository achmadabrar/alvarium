package com.bs.ecommerce.account.review.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.ProductReviewItem
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
    val productId: Long?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("ReviewTypeList")
    val reviewTypeList: List<Any>?
)