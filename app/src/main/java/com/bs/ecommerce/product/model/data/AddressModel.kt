package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.google.gson.annotations.SerializedName

data class AddressModel(
    @SerializedName("Address1")
    var address1: String?,
    @SerializedName("Address2")
    var address2: String?,
    @SerializedName("AvailableCountries")
    var availableCountries: List<AvailableCountry>?,
    @SerializedName("AvailableStates")
    var availableStates: List<AvailableCountry>?,
    @SerializedName("City")
    var city: String?,
    @SerializedName("CityEnabled")
    var cityEnabled: Boolean?,
    @SerializedName("CityRequired")
    var cityRequired: Boolean?,
    @SerializedName("Company")
    var company: String?,
    @SerializedName("CompanyEnabled")
    var companyEnabled: Boolean?,
    @SerializedName("CompanyRequired")
    var companyRequired: Boolean?,
    @SerializedName("CountryEnabled")
    var countryEnabled: Boolean?,
    @SerializedName("CountryId")
    var countryId: Int?,
    @SerializedName("CountryName")
    var countryName: String?,
    @SerializedName("County")
    var county: Any?,
    @SerializedName("CountyEnabled")
    var countyEnabled: Boolean?,
    @SerializedName("CountyRequired")
    var countyRequired: Boolean?,
    @SerializedName("CustomAddressAttributes")
    var customAddressAttributes: List<Any>?,
    @SerializedName("CustomProperties")
    var customProperties: CustomProperties?,
    @SerializedName("Email")
    var email: String?,
    @SerializedName("FaxEnabled")
    var faxEnabled: Boolean?,
    @SerializedName("FaxNumber")
    var faxNumber: String?,
    @SerializedName("FaxRequired")
    var faxRequired: Boolean?,
    @SerializedName("FirstName")
    var firstName: String?,
    @SerializedName("FormattedCustomAddressAttributes")
    var formattedCustomAddressAttributes: String?,
    @SerializedName("Id")
    var id: Int?,
    @SerializedName("LastName")
    var lastName: String?,
    @SerializedName("PhoneEnabled")
    var phoneEnabled: Boolean?,
    @SerializedName("PhoneNumber")
    var phoneNumber: String?,
    @SerializedName("PhoneRequired")
    var phoneRequired: Boolean?,
    @SerializedName("StateProvinceEnabled")
    var stateProvinceEnabled: Boolean?,
    @SerializedName("StateProvinceId")
    var stateProvinceId: Any?,
    @SerializedName("StateProvinceName")
    var stateProvinceName: String?,
    @SerializedName("StreetAddress2Enabled")
    var streetAddress2Enabled: Boolean?,
    @SerializedName("StreetAddress2Required")
    var streetAddress2Required: Boolean?,
    @SerializedName("StreetAddressEnabled")
    var streetAddressEnabled: Boolean?,
    @SerializedName("StreetAddressRequired")
    var streetAddressRequired: Boolean?,
    @SerializedName("ZipPostalCode")
    var zipPostalCode: String?,
    @SerializedName("ZipPostalCodeEnabled")
    var zipPostalCodeEnabled: Boolean?,
    @SerializedName("ZipPostalCodeRequired")
    var zipPostalCodeRequired: Boolean?
)