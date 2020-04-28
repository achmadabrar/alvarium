package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName


class BillingAddressResponse(
    @SerializedName("ExistingAddresses") var existingAddresses: List<BillingAddress>?,
    @SerializedName("NewAddress") var newAddress: BillingAddress?,
    @SerializedName("IsNewAddressPreselected") var isNewAddressPreselected: Boolean
)
    : BaseResponse()

data class BillingAddress(
    @SerializedName("Address1") var address1: Any = Any(),
    @SerializedName("Address2") var address2: Any = Any(),
    @SerializedName("AvailableCountries") var availableCountries: List<AvailableCountry> = listOf(),
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
    @SerializedName("CustomAddressAttributes") var customAddressAttributes: List<Any> = listOf(),
    @SerializedName("Email") var email: Any = Any(),
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxNumber") var faxNumber: Any = Any(),
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: Any = Any(),
    @SerializedName("Form") var form: Any = Any(),
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

data class AvailableCountry(
    @SerializedName("Disabled") var disabled: Boolean = false,
    @SerializedName("Group") var group: Any = Any(),
    @SerializedName("Selected") var selected: Boolean = false,
    @SerializedName("Text") var text: String = "",
    @SerializedName("Value") var value: String = ""
)

