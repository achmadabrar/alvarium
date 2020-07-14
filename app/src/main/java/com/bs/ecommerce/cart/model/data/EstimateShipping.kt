package com.bs.ecommerce.cart.model.data

import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.google.gson.annotations.SerializedName

data class EstimateShipping(
    @SerializedName("AvailableCountries") var availableCountries: List<AvailableCountry> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<AvailableState> = listOf(),
    @SerializedName("CountryId") var countryId: Int = 0,
    @SerializedName("Enabled") var enabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Int = 0,
    @SerializedName("ZipPostalCode") var zipPostalCode: String = ""
)

data class EstimateShippingReqBody(
    @SerializedName("Data") var estimateShippping: EstimateShipping?
)