package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName


data class AddToCart(
    @SerializedName("AllowedQuantities")
    val allowedQuantities: List<Int>?,
    @SerializedName("AvailableForPreOrder")
    val availableForPreOrder: Boolean?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("CustomerEnteredPrice")
    var customerEnteredPrice: Double?,
    @SerializedName("CustomerEnteredPriceRange")
    val customerEnteredPriceRange: String?,
    @SerializedName("CustomerEntersPrice")
    val customerEntersPrice: Boolean?,
    @SerializedName("DisableBuyButton")
    val disableBuyButton: Boolean?,
    @SerializedName("DisableWishlistButton")
    val disableWishlistButton: Boolean?,
    @SerializedName("EnteredQuantity")
    var enteredQuantity: Int?,
    @SerializedName("IsRental")
    val isRental: Boolean?,
    @SerializedName("MinimumQuantityNotification")
    val minimumQuantityNotification: String?,
    @SerializedName("PreOrderAvailabilityStartDateTimeUserTime")
    val preOrderAvailabilityStartDateTimeUserTime: Any?,
    @SerializedName("PreOrderAvailabilityStartDateTimeUtc")
    val preOrderAvailabilityStartDateTimeUtc: Any?,
    @SerializedName("ProductId")
    val productId: Long?,
    @SerializedName("UpdateShoppingCartItemType")
    val updateShoppingCartItemType: Any?,
    @SerializedName("UpdatedShoppingCartItemId")
    val updatedShoppingCartItemId: Int?
)