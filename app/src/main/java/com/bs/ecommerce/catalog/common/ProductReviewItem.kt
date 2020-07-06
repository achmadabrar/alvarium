package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.Helpfulness
import com.google.gson.annotations.SerializedName

data class ProductReviewItem(
    @SerializedName("AdditionalProductReviewList")
    val additionalProductReviewList: List<Any>?,
    @SerializedName("AllowViewingProfiles")
    val allowViewingProfiles: Boolean?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("CustomerId")
    val customerId: Long?,
    @SerializedName("CustomerName")
    val customerName: String?,
    @SerializedName("CustomerAvatarUrl")
    val customerAvatarUrl: String?,
    @SerializedName("Helpfulness")
    var helpfulness: Helpfulness?,
    @SerializedName("Id")
    val id: Long?,
    @SerializedName("Rating")
    val rating: Int?,
    @SerializedName("ReplyText")
    val replyText: String?,
    @SerializedName("ReviewText")
    val reviewText: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("WrittenOnStr")
    val writtenOnStr: String?
) {
    fun isEquals(id: Long?) : Boolean = this.id == id
}