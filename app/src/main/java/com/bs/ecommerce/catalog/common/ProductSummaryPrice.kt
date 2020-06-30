package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class ProductSummaryPrice(
    @SerializedName("AvailableForPreOrder")
    val availableForPreOrder: Boolean?,
    @SerializedName("BasePricePAngV")
    val basePricePAngV: Any?,
    @SerializedName("DisableAddToCompareListButton")
    val disableAddToCompareListButton: Boolean?,
    @SerializedName("DisableBuyButton")
    val disableBuyButton: Boolean?,
    @SerializedName("DisableWishlistButton")
    val disableWishlistButton: Boolean?,
    @SerializedName("DisplayTaxShippingInfo")
    val displayTaxShippingInfo: Boolean?,
    @SerializedName("ForceRedirectionAfterAddingToCart")
    val forceRedirectionAfterAddingToCart: Boolean?,
    @SerializedName("IsRental")
    val isRental: Boolean?,
    @SerializedName("OldPrice")
    val oldPrice: String?,
    @SerializedName("PreOrderAvailabilityStartDateTimeUtc")
    val preOrderAvailabilityStartDateTimeUtc: String?,
    @SerializedName("Price")
    val price: String?,
    @SerializedName("PriceValue")
    val priceValue: Double?
)