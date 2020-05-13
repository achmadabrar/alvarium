package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ProductPrice(
    @SerializedName("BasePricePAngV")
    val basePricePAngV: Any?,
    @SerializedName("CallForPrice")
    val callForPrice: Boolean?,
    @SerializedName("CurrencyCode")
    val currencyCode: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("CustomerEntersPrice")
    val customerEntersPrice: Boolean?,
    @SerializedName("DisplayTaxShippingInfo")
    val displayTaxShippingInfo: Boolean?,
    @SerializedName("HidePrices")
    val hidePrices: Boolean?,
    @SerializedName("IsRental")
    val isRental: Boolean?,
    @SerializedName("OldPrice")
    val oldPrice: String?,
    @SerializedName("Price")
    val price: String?,
    @SerializedName("PriceValue")
    val priceValue: Double?,
    @SerializedName("PriceWithDiscount")
    val priceWithDiscount: String?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("RentalPrice")
    val rentalPrice: String?
)
