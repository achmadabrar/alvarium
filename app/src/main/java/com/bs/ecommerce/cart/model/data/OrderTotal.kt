package com.bs.ecommerce.cart.model.data

import com.google.gson.annotations.SerializedName

data class OrderTotal(

    @SerializedName("DisplayTax") var displayTax: Boolean = false,
    @SerializedName("DisplayTaxRates") var displayTaxRates: Boolean = false,
    @SerializedName("GiftCards") var giftCards : List<GiftCard>? = null,
    @SerializedName("HideShippingTotal") var hideShippingTotal: Boolean = false,
    @SerializedName("IsEditable") var isEditable: Boolean = false,
    @SerializedName("OrderTotal") var orderTotal: String = "",
    @SerializedName("OrderTotalDiscount") var orderTotalDiscount: String? = null,
    @SerializedName("PaymentMethodAdditionalFee") var paymentMethodAdditionalFee: Any = Any(),
    @SerializedName("RedeemedRewardPoints") var redeemedRewardPoints: Int = 0,
    @SerializedName("RedeemedRewardPointsAmount") var redeemedRewardPointsAmount: Any = Any(),
    @SerializedName("RequiresShipping") var requiresShipping: Boolean = false,
    @SerializedName("SelectedShippingMethod") var selectedShippingMethod: Any = Any(),
    @SerializedName("Shipping") var shipping: String = "",
    @SerializedName("SubTotal") var subTotal: String = "",
    @SerializedName("SubTotalDiscount") var subTotalDiscount: String? = null,
    @SerializedName("Tax") var tax: String? = null,
    @SerializedName("TaxRates") var taxRates: List<TaxRate>? = null,
    @SerializedName("WillEarnRewardPoints") var willEarnRewardPoints: Int = 0
)


class GiftCard
{
    @SerializedName("CouponCode") var couponCode: String? = null
    @SerializedName("Amount") var amount: String? = null
    @SerializedName("Id") var id: String? = null
    @SerializedName("Remaining") var remaining: String? = null
}

data class TaxRate(
    @SerializedName("Rate") var rate: String = "",
    @SerializedName("Value") var value: String = ""
)