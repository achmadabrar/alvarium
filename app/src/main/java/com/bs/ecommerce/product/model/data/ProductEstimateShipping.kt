package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.google.gson.annotations.SerializedName

data class ProductEstimateShipping(
    @SerializedName("AvailableCountries")
    val availableCountries: List<AvailableCountry>?,
    @SerializedName("AvailableStates")
    val availableStates: List<AvailableState>?,
    @SerializedName("CountryId")
    val countryId: Int?,
    @SerializedName("Enabled")
    val enabled: Boolean?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("StateProvinceId")
    val stateProvinceId: Int?,
    @SerializedName("ZipPostalCode")
    val zipPostalCode: Any?
)