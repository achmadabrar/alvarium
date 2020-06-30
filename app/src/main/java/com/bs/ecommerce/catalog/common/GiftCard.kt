package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class GiftCard(
    @SerializedName("CustomProperties")
    var customProperties: CustomProperties?,
    @SerializedName("GiftCardType")
    var giftCardType: Int?,
    @SerializedName("IsGiftCard")
    var isGiftCard: Boolean?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("RecipientEmail")
    var recipientEmail: String?,
    @SerializedName("RecipientName")
    var recipientName: String?,
    @SerializedName("SenderEmail")
    var senderEmail: String?,
    @SerializedName("SenderName")
    var senderName: String?
)