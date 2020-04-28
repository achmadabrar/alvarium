package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("Address1")
    val address1: String?,
    @SerializedName("Address2")
    val address2: String?,
    @SerializedName("AvailableCountries")
    val availableCountries: List<Any>?,
    @SerializedName("AvailableStates")
    val availableStates: List<Any>?,
    @SerializedName("City")
    val city: String?,
    @SerializedName("CityEnabled")
    val cityEnabled: Boolean?,
    @SerializedName("CityRequired")
    val cityRequired: Boolean?,
    @SerializedName("Company")
    val company: String?,
    @SerializedName("CompanyEnabled")
    val companyEnabled: Boolean?,
    @SerializedName("CompanyRequired")
    val companyRequired: Boolean?,
    @SerializedName("CountryEnabled")
    val countryEnabled: Boolean?,
    @SerializedName("CountryId")
    val countryId: Int?,
    @SerializedName("CountryName")
    val countryName: String?,
    @SerializedName("County")
    val county: Any?,
    @SerializedName("CountyEnabled")
    val countyEnabled: Boolean?,
    @SerializedName("CountyRequired")
    val countyRequired: Boolean?,
    @SerializedName("CustomAddressAttributes")
    val customAddressAttributes: List<Any>?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FaxEnabled")
    val faxEnabled: Boolean?,
    @SerializedName("FaxNumber")
    val faxNumber: String?,
    @SerializedName("FaxRequired")
    val faxRequired: Boolean?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("FormattedCustomAddressAttributes")
    val formattedCustomAddressAttributes: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("PhoneEnabled")
    val phoneEnabled: Boolean?,
    @SerializedName("PhoneNumber")
    val phoneNumber: String?,
    @SerializedName("PhoneRequired")
    val phoneRequired: Boolean?,
    @SerializedName("StateProvinceEnabled")
    val stateProvinceEnabled: Boolean?,
    @SerializedName("StateProvinceId")
    val stateProvinceId: Any?,
    @SerializedName("StateProvinceName")
    val stateProvinceName: String?,
    @SerializedName("StreetAddress2Enabled")
    val streetAddress2Enabled: Boolean?,
    @SerializedName("StreetAddress2Required")
    val streetAddress2Required: Boolean?,
    @SerializedName("StreetAddressEnabled")
    val streetAddressEnabled: Boolean?,
    @SerializedName("StreetAddressRequired")
    val streetAddressRequired: Boolean?,
    @SerializedName("ZipPostalCode")
    val zipPostalCode: String?,
    @SerializedName("ZipPostalCodeEnabled")
    val zipPostalCodeEnabled: Boolean?,
    @SerializedName("ZipPostalCodeRequired")
    val zipPostalCodeRequired: Boolean?
)