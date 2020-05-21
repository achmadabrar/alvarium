package com.bs.ecommerce.checkout.model.data
import com.bs.ecommerce.cart.model.data.CartInfoData
import com.bs.ecommerce.cart.model.data.OrderTotal
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class ConfirmOrderResponse(@SerializedName("Data") var data: ConfirmOrderData = ConfirmOrderData()) : BaseResponse()

data class ConfirmOrderData(
    @SerializedName("Cart") var cart: CartInfoData = CartInfoData(),
    @SerializedName("Confirm") var confirm: Confirm = Confirm(),
    @SerializedName("EstimateShipping") var estimateShipping: EstimateShipping = EstimateShipping(),
    @SerializedName("OrderTotals") var orderTotals: OrderTotal = OrderTotal(),
    @SerializedName("SelectedCheckoutAttributes") var selectedCheckoutAttributes: String = ""
)


data class Confirm(
    @SerializedName("MinOrderTotalWarning") var minOrderTotalWarning: Any = Any(),
    @SerializedName("TermsOfServiceOnOrderConfirmPage") var termsOfServiceOnOrderConfirmPage: Boolean = false,
    @SerializedName("TermsOfServicePopup") var termsOfServicePopup: Boolean = false,
    @SerializedName("Warnings") var warnings: List<Any> = listOf()
)

data class EstimateShipping(
    @SerializedName("AvailableCountries") var availableCountries: List<AvailableCountry> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<AvailableState> = listOf(),
    @SerializedName("CountryId") var countryId: Any = Any(),
    @SerializedName("Enabled") var enabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Any = Any(),
    @SerializedName("ZipPostalCode") var zipPostalCode: String = ""
)
