package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ProductPrice(
    @SerializedName("AvailableForPreOrder")
        val availableForPreOrder: Boolean?,
    @SerializedName("CustomProperties")
        val customProperties: CustomProperties?,
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
    @SerializedName("Price")
        val price: String?,
    @SerializedName("PriceValue")
        val priceValue: Double?
)
