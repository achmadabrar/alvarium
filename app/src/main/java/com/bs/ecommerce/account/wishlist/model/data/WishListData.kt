package com.bs.ecommerce.account.wishlist.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class WishListData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("CustomerFullname")
    val customerFullname: String?,
    @SerializedName("CustomerGuid")
    val customerGuid: String?,
    @SerializedName("DisplayAddToCart")
    val displayAddToCart: Boolean?,
    @SerializedName("DisplayTaxShippingInfo")
    val displayTaxShippingInfo: Boolean?,
    @SerializedName("EmailWishlistEnabled")
    val emailWishlistEnabled: Boolean?,
    @SerializedName("IsEditable")
    val isEditable: Boolean?,
    @SerializedName("Items")
    var items: List<WishListItem>?,
    @SerializedName("ShowProductImages")
    val showProductImages: Boolean?,
    @SerializedName("ShowSku")
    val showSku: Boolean?,
    @SerializedName("Warnings")
    val warnings: List<Any>?
)