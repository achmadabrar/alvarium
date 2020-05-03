package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName



data class BillingAddressResponse(@SerializedName("Data") var data: BillingAddressData = BillingAddressData()) : BaseResponse()

data class BillingAddressData(
    @SerializedName("BillingAddress") var billingAddress: BillingAddress = BillingAddress(),
    @SerializedName("DisableBillingAddressCheckoutStep") var disableBillingAddressCheckoutStep: Boolean = false,
    @SerializedName("ShippingRequired") var shippingRequired: Boolean = false
)

data class BillingAddress(
    @SerializedName("BillingNewAddress") var billingNewAddress: BillingNewAddress = BillingNewAddress(),
    @SerializedName("ExistingAddresses") var existingAddresses: List<BillingNewAddress> = listOf(),
    @SerializedName("InvalidExistingAddresses") var invalidExistingAddresses: List<Any> = listOf(),
    @SerializedName("NewAddressPreselected") var newAddressPreselected: Boolean = false,
    @SerializedName("ShipToSameAddress") var shipToSameAddress: Boolean = false,
    @SerializedName("ShipToSameAddressAllowed") var shipToSameAddressAllowed: Boolean = false
)



data class BillingNewAddress(
    @SerializedName("Address1") var address1: Any = Any(),
    @SerializedName("Address2") var address2: Any = Any(),
    @SerializedName("AvailableCountries") var availableCountries: List<AvailableCountry>? = null,
    @SerializedName("AvailableStates") var availableStates: List<AvailableCountry> = listOf(),
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
    @SerializedName("Email") var email: String = "",
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxNumber") var faxNumber: Any = Any(),
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: String = "",
    @SerializedName("FormattedCustomAddressAttributes") var formattedCustomAddressAttributes: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("LastName") var lastName: String = "",
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



data class AvailableCountry(
    @SerializedName("Disabled") var disabled: Boolean = false,
    @SerializedName("Group") var group: Any = Any(),
    @SerializedName("Selected") var selected: Boolean = false,
    @SerializedName("Text") var text: String = "",
    @SerializedName("Value") var value: String = ""
)


