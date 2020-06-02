package com.bs.ecommerce.cart.model.data

import com.bs.ecommerce.product.model.data.CheckoutAttribute
import com.google.gson.annotations.SerializedName

data class CartInfoData(
    @SerializedName("ButtonPaymentMethodViewComponentNames") var buttonPaymentMethodViewComponentNames: List<Any> = listOf(),
    @SerializedName("CheckoutAttributes") var checkoutAttributes: List<CheckoutAttribute> = listOf(),
    @SerializedName("DiscountBox") var discountBox: DiscountBox = DiscountBox(),
    @SerializedName("DisplayTaxShippingInfo") var displayTaxShippingInfo: Boolean = false,
    @SerializedName("GiftCardBox") var giftCardBox: GiftCardBox = GiftCardBox(),
    @SerializedName("HideCheckoutButton") var hideCheckoutButton: Boolean = false,
    @SerializedName("IsEditable") var isEditable: Boolean = false,
    @SerializedName("Items") var items: List<CartProduct> = listOf(),
    @SerializedName("MinOrderSubtotalWarning") var minOrderSubtotalWarning: Any = Any(),
    @SerializedName("OnePageCheckoutEnabled") var onePageCheckoutEnabled: Boolean = false,
    @SerializedName("OrderReviewData") var orderReviewData: OrderReviewData = OrderReviewData(),
    @SerializedName("ShowProductImages") var showProductImages: Boolean = false,
    @SerializedName("ShowSku") var showSku: Boolean = false,
    @SerializedName("ShowVendorName") var showVendorName: Boolean = false,
    @SerializedName("TermsOfServiceOnOrderConfirmPage") var termsOfServiceOnOrderConfirmPage: Boolean = false,
    @SerializedName("TermsOfServiceOnShoppingCartPage") var termsOfServiceOnShoppingCartPage: Boolean = false,
    @SerializedName("TermsOfServicePopup") var termsOfServicePopup: Boolean = false,
    @SerializedName("Warnings") var warnings: List<Any> = listOf()
)


data class AppliedDiscountsWithCode(@SerializedName("CouponCode") var couponCode: String = "", @SerializedName("Id") var id: Int = 0)

data class DiscountBox(
    @SerializedName("AppliedDiscountsWithCodes") var appliedDiscountsWithCodes: List<AppliedDiscountsWithCode> = listOf(),
    @SerializedName("Display") var display: Boolean = false,
    @SerializedName("IsApplied") var isApplied: Boolean = false,
    @SerializedName("Messages") var messages: List<String> = listOf()
)

data class GiftCardBox(
    @SerializedName("Display") var display: Boolean = false,
    @SerializedName("IsApplied") var isApplied: Boolean = false,
    @SerializedName("Message") var message: String? = null
)


data class OrderReviewData(
    @SerializedName("BillingAddress") var billingAddress: OrderReviewAddressModel = OrderReviewAddressModel(),
    @SerializedName("Display") var display: Boolean = false,
    @SerializedName("IsShippable") var isShippable: Boolean = false,
    @SerializedName("PaymentMethod") var paymentMethod: String = String(),
    @SerializedName("PickupAddress") var pickupAddress: OrderReviewAddressModel = OrderReviewAddressModel(),
    @SerializedName("SelectedPickupInStore") var selectedPickupInStore: Boolean = false,
    @SerializedName("ShippingAddress") var shippingAddress: OrderReviewAddressModel = OrderReviewAddressModel(),
    @SerializedName("ShippingMethod") var shippingMethod: String = String()
)

data class OrderReviewAddressModel(
    @SerializedName("Address1") var address1: String? = "",
    @SerializedName("Address2") var address2: String? = "",
    @SerializedName("AvailableCountries") var availableCountries: List<Any> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<Any> = listOf(),
    @SerializedName("City") var city: Any = Any(),
    @SerializedName("CityEnabled") var cityEnabled: Boolean = false,
    @SerializedName("CityRequired") var cityRequired: Boolean = false,
    @SerializedName("Company") var company: Any = Any(),
    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false,
    @SerializedName("CompanyRequired") var companyRequired: Boolean = false,
    @SerializedName("CountryEnabled") var countryEnabled: Boolean = false,
    @SerializedName("CountryId") var countryId: Any = Any(),
    @SerializedName("CountryName") var countryName: Any = Any(),
    @SerializedName("County") var county: Any = Any(),
    @SerializedName("CountyEnabled") var countyEnabled: Boolean = false,
    @SerializedName("CountyRequired") var countyRequired: Boolean = false,
    @SerializedName("CustomAddressAttributes") var customAddressAttributes: List<Any> = listOf(),
    @SerializedName("Email") var email: Any = Any(),
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxNumber") var faxNumber: Any = Any(),
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: Any = Any(),
    @SerializedName("FormattedCustomAddressAttributes") var formattedCustomAddressAttributes: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("LastName") var lastName: Any = Any(),
    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false,
    @SerializedName("PhoneNumber") var phoneNumber: Any = Any(),
    @SerializedName("PhoneRequired") var phoneRequired: Boolean = false,
    @SerializedName("StateProvinceEnabled") var stateProvinceEnabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Any = Any(),
    @SerializedName("StateProvinceName") var stateProvinceName: Any = Any(),
    @SerializedName("StreetAddress2Enabled") var streetAddress2Enabled: Boolean = false,
    @SerializedName("StreetAddress2Required") var streetAddress2Required: Boolean = false,
    @SerializedName("StreetAddressEnabled") var streetAddressEnabled: Boolean = false,
    @SerializedName("StreetAddressRequired") var streetAddressRequired: Boolean = false,
    @SerializedName("ZipPostalCode") var zipPostalCode: Any = Any(),
    @SerializedName("ZipPostalCodeEnabled") var zipPostalCodeEnabled: Boolean = false,
    @SerializedName("ZipPostalCodeRequired") var zipPostalCodeRequired: Boolean = false
)



/*
data class PickupAddress(
    @SerializedName("Address1") var address1: Any = Any(),
    @SerializedName("Address2") var address2: Any = Any(),
    @SerializedName("AvailableCountries") var availableCountries: List<Any> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<Any> = listOf(),
    @SerializedName("City") var city: Any = Any(),
    @SerializedName("CityEnabled") var cityEnabled: Boolean = false,
    @SerializedName("CityRequired") var cityRequired: Boolean = false,
    @SerializedName("Company") var company: Any = Any(),
    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false,
    @SerializedName("CompanyRequired") var companyRequired: Boolean = false,
    @SerializedName("CountryEnabled") var countryEnabled: Boolean = false,
    @SerializedName("CountryId") var countryId: Any = Any(),
    @SerializedName("CountryName") var countryName: Any = Any(),
    @SerializedName("County") var county: Any = Any(),
    @SerializedName("CountyEnabled") var countyEnabled: Boolean = false,
    @SerializedName("CountyRequired") var countyRequired: Boolean = false,
    @SerializedName("CustomAddressAttributes") var customAddressAttributes: List<Any> = listOf(),
    @SerializedName("Email") var email: Any = Any(),
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxNumber") var faxNumber: Any = Any(),
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: Any = Any(),
    @SerializedName("FormattedCustomAddressAttributes") var formattedCustomAddressAttributes: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("LastName") var lastName: Any = Any(),
    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false,
    @SerializedName("PhoneNumber") var phoneNumber: Any = Any(),
    @SerializedName("PhoneRequired") var phoneRequired: Boolean = false,
    @SerializedName("StateProvinceEnabled") var stateProvinceEnabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Any = Any(),
    @SerializedName("StateProvinceName") var stateProvinceName: Any = Any(),
    @SerializedName("StreetAddress2Enabled") var streetAddress2Enabled: Boolean = false,
    @SerializedName("StreetAddress2Required") var streetAddress2Required: Boolean = false,
    @SerializedName("StreetAddressEnabled") var streetAddressEnabled: Boolean = false,
    @SerializedName("StreetAddressRequired") var streetAddressRequired: Boolean = false,
    @SerializedName("ZipPostalCode") var zipPostalCode: Any = Any(),
    @SerializedName("ZipPostalCodeEnabled") var zipPostalCodeEnabled: Boolean = false,
    @SerializedName("ZipPostalCodeRequired") var zipPostalCodeRequired: Boolean = false
)

data class ShippingAddress(
    @SerializedName("Address1") var address1: Any = Any(),
    @SerializedName("Address2") var address2: Any = Any(),
    @SerializedName("AvailableCountries") var availableCountries: List<Any> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<Any> = listOf(),
    @SerializedName("City") var city: Any = Any(),
    @SerializedName("CityEnabled") var cityEnabled: Boolean = false,
    @SerializedName("CityRequired") var cityRequired: Boolean = false,
    @SerializedName("Company") var company: Any = Any(),
    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false,
    @SerializedName("CompanyRequired") var companyRequired: Boolean = false,
    @SerializedName("CountryEnabled") var countryEnabled: Boolean = false,
    @SerializedName("CountryId") var countryId: Any = Any(),
    @SerializedName("CountryName") var countryName: Any = Any(),
    @SerializedName("County") var county: Any = Any(),
    @SerializedName("CountyEnabled") var countyEnabled: Boolean = false,
    @SerializedName("CountyRequired") var countyRequired: Boolean = false,
    @SerializedName("CustomAddressAttributes") var customAddressAttributes: List<Any> = listOf(),
    @SerializedName("Email") var email: Any = Any(),
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxNumber") var faxNumber: Any = Any(),
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: Any = Any(),
    @SerializedName("FormattedCustomAddressAttributes") var formattedCustomAddressAttributes: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("LastName") var lastName: Any = Any(),
    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false,
    @SerializedName("PhoneNumber") var phoneNumber: Any = Any(),
    @SerializedName("PhoneRequired") var phoneRequired: Boolean = false,
    @SerializedName("StateProvinceEnabled") var stateProvinceEnabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Any = Any(),
    @SerializedName("StateProvinceName") var stateProvinceName: Any = Any(),
    @SerializedName("StreetAddress2Enabled") var streetAddress2Enabled: Boolean = false,
    @SerializedName("StreetAddress2Required") var streetAddress2Required: Boolean = false,
    @SerializedName("StreetAddressEnabled") var streetAddressEnabled: Boolean = false,
    @SerializedName("StreetAddressRequired") var streetAddressRequired: Boolean = false,
    @SerializedName("ZipPostalCode") var zipPostalCode: Any = Any(),
    @SerializedName("ZipPostalCodeEnabled") var zipPostalCodeEnabled: Boolean = false,
    @SerializedName("ZipPostalCodeRequired") var zipPostalCodeRequired: Boolean = false
)*/
