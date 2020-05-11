package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class GiftCard(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("GiftCardType")
    val giftCardType: Int?,
    @SerializedName("IsGiftCard")
    val isGiftCard: Boolean?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("RecipientEmail")
    val recipientEmail: String?,
    @SerializedName("RecipientName")
    val recipientName: String?,
    @SerializedName("SenderEmail")
    val senderEmail: String?,
    @SerializedName("SenderName")
    val senderName: String?
)