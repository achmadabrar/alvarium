package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class SaveBillingResponse(@SerializedName("Data") var data: SaveBillingData = SaveBillingData()) : BaseResponse()

data class SaveBillingData(
    @SerializedName("BillingAddressModel") var billingAddressModel: BillingAddress = BillingAddress(),
    @SerializedName("ConfirmModel") var confirmModel: ConfirmModel = ConfirmModel(),
    @SerializedName("NextStep") var nextStep: Int = 0,
    @SerializedName("PaymentInfoModel") var paymentInfoModel: PaymentInfoModel = PaymentInfoModel(),
    @SerializedName("PaymentMethodModel") var paymentMethodModel: PaymentMethodModel = PaymentMethodModel(),
    @SerializedName("ShippingAddressModel") var shippingAddressModel: ShippingAddressModel = ShippingAddressModel(),
    @SerializedName("ShippingMethodModel") var shippingMethodModel: ShippingMethodModel = ShippingMethodModel()
)

data class ConfirmModel(
    @SerializedName("MinOrderTotalWarning") var minOrderTotalWarning: Any = Any(),
    @SerializedName("TermsOfServiceOnOrderConfirmPage") var termsOfServiceOnOrderConfirmPage: Boolean = false,
    @SerializedName("TermsOfServicePopup") var termsOfServicePopup: Boolean = false,
    @SerializedName("Warnings") var warnings: List<Any> = listOf()
)

data class PaymentInfoModel(
    @SerializedName("DisplayOrderTotals") var displayOrderTotals: Boolean = false,
    @SerializedName("PaymentViewComponentName") var paymentViewComponentName: Any = Any()
)

data class PaymentMethodModel(
    @SerializedName("DisplayRewardPoints") var displayRewardPoints: Boolean = false,
    @SerializedName("PaymentMethods") var paymentMethods: List<Any> = listOf(),
    @SerializedName("RewardPointsAmount") var rewardPointsAmount: Any = Any(),
    @SerializedName("RewardPointsBalance") var rewardPointsBalance: Int = 0,
    @SerializedName("RewardPointsEnoughToPayForOrder") var rewardPointsEnoughToPayForOrder: Boolean = false,
    @SerializedName("UseRewardPoints") var useRewardPoints: Boolean = false
)

data class ShippingAddressModel(
    @SerializedName("AllowPickupInStore") var allowPickupInStore: Boolean = false,
    @SerializedName("DisplayPickupPointsOnMap") var displayPickupPointsOnMap: Boolean = false,
    @SerializedName("ExistingAddresses") var existingAddresses: List<Any> = listOf(),
    @SerializedName("GoogleMapsApiKey") var googleMapsApiKey: Any = Any(),
    @SerializedName("InvalidExistingAddresses") var invalidExistingAddresses: List<Any> = listOf(),
    @SerializedName("NewAddressPreselected") var newAddressPreselected: Boolean = false,
    @SerializedName("PickupInStore") var pickupInStore: Boolean = false,
    @SerializedName("PickupInStoreOnly") var pickupInStoreOnly: Boolean = false,
    @SerializedName("PickupPoints") var pickupPoints: List<Any> = listOf(),
    @SerializedName("ShippingNewAddress") var shippingNewAddress: BillingNewAddress = BillingNewAddress(),
    @SerializedName("Warnings") var warnings: List<Any> = listOf()
)

data class ShippingMethodModel(
    @SerializedName("NotifyCustomerAboutShippingFromMultipleLocations") var notifyCustomerAboutShippingFromMultipleLocations: Boolean = false,
    @SerializedName("ShippingMethods") var shippingMethods: List<Any> = listOf(),
    @SerializedName("Warnings") var warnings: List<Any> = listOf()
)