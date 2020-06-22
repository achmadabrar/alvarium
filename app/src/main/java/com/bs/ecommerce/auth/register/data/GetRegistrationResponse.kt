package com.bs.ecommerce.auth.register.data
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.product.model.data.CustomerAttribute
import com.google.gson.annotations.SerializedName


data class GetRegistrationResponse(@SerializedName("Data") var data: GetRegisterData = GetRegisterData()) : BaseResponse()

data class GetRegisterData(
    @SerializedName("AcceptPrivacyPolicyEnabled") var acceptPrivacyPolicyEnabled: Boolean = false,
    @SerializedName("AcceptPrivacyPolicyPopup") var acceptPrivacyPolicyPopup: Boolean = false,
    @SerializedName("AllowCustomersToSetTimeZone") var allowCustomersToSetTimeZone: Boolean = false,
    @SerializedName("AvailableCountries") var availableCountries: List<AvailableCountry> = listOf(),
    @SerializedName("AvailableStates") var availableStates: List<AvailableState> = listOf(),
    @SerializedName("AvailableTimeZones") var availableTimeZones: List<AvailableTimeZone> = listOf(),
    @SerializedName("CheckUsernameAvailabilityEnabled") var checkUsernameAvailabilityEnabled: Boolean = false,
    @SerializedName("City") var city: String = "",
    @SerializedName("CityEnabled") var cityEnabled: Boolean = false,
    @SerializedName("CityRequired") var cityRequired: Boolean = false,
    @SerializedName("Company") var company: String = "",
    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false,
    @SerializedName("CompanyRequired") var companyRequired: Boolean = false,
    @SerializedName("ConfirmEmail") var confirmEmail: String = "",
    @SerializedName("ConfirmPassword") var confirmPassword: String = "",
    @SerializedName("CountryEnabled") var countryEnabled: Boolean = false,
    @SerializedName("CountryId") var countryId: Int = 0,
    @SerializedName("CountryRequired") var countryRequired: Boolean = false,
    @SerializedName("County") var county: String = "",
    @SerializedName("CountyEnabled") var countyEnabled: Boolean = false,
    @SerializedName("CountyRequired") var countyRequired: Boolean = false,
    @SerializedName("CustomerAttributes") var customerAttributes: List<CustomerAttribute>? = listOf(),
    @SerializedName("DateOfBirthDay") var dateOfBirthDay: Int = 0,
    @SerializedName("DateOfBirthEnabled") var dateOfBirthEnabled: Boolean = false,
    @SerializedName("DateOfBirthMonth") var dateOfBirthMonth: Int = 0,
    @SerializedName("DateOfBirthRequired") var dateOfBirthRequired: Boolean = false,
    @SerializedName("DateOfBirthYear") var dateOfBirthYear: Int = 0,
    @SerializedName("DisplayCaptcha") var displayCaptcha: Boolean = false,
    @SerializedName("DisplayVatNumber") var displayVatNumber: Boolean = false,
    @SerializedName("Email") var email: String = "",
    @SerializedName("EnteringEmailTwice") var enteringEmailTwice: Boolean = false,
    @SerializedName("Fax") var fax: String = "",
    @SerializedName("FaxEnabled") var faxEnabled: Boolean = false,
    @SerializedName("FaxRequired") var faxRequired: Boolean = false,
    @SerializedName("FirstName") var firstName: String = "",
    @SerializedName("GdprConsents") var gdprConsents: List<Any> = listOf(),
    @SerializedName("Gender") var gender: String = "",
    @SerializedName("GenderEnabled") var genderEnabled: Boolean = false,
    @SerializedName("HoneypotEnabled") var honeypotEnabled: Boolean = false,
    @SerializedName("LastName") var lastName: String = "",
    @SerializedName("Newsletter") var newsletter: Boolean = false,
    @SerializedName("NewsletterEnabled") var newsletterEnabled: Boolean = false,
    @SerializedName("Password") var password: String = "",
    @SerializedName("Phone") var phone: String = "",
    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false,
    @SerializedName("PhoneRequired") var phoneRequired: Boolean = false,
    @SerializedName("StateProvinceEnabled") var stateProvinceEnabled: Boolean = false,
    @SerializedName("StateProvinceId") var stateProvinceId: Int = 0,
    @SerializedName("StateProvinceRequired") var stateProvinceRequired: Boolean = false,
    @SerializedName("StreetAddress") var streetAddress: String = "",
    @SerializedName("StreetAddress2") var streetAddress2: String = "",
    @SerializedName("StreetAddress2Enabled") var streetAddress2Enabled: Boolean = false,
    @SerializedName("StreetAddress2Required") var streetAddress2Required: Boolean = false,
    @SerializedName("StreetAddressEnabled") var streetAddressEnabled: Boolean = false,
    @SerializedName("StreetAddressRequired") var streetAddressRequired: Boolean = false,
    @SerializedName("TimeZoneId") var timeZoneId: String = "",
    @SerializedName("Username") var username: String = "",
    @SerializedName("UsernamesEnabled") var usernamesEnabled: Boolean = false,
    @SerializedName("VatNumber") var vatNumber: String = "",
    @SerializedName("ZipPostalCode") var zipPostalCode: String = "",
    @SerializedName("ZipPostalCodeEnabled") var zipPostalCodeEnabled: Boolean = false,
    @SerializedName("ZipPostalCodeRequired") var zipPostalCodeRequired: Boolean = false/*,
    @SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf()*/
)

data class AvailableTimeZone(
    @SerializedName("Disabled") var disabled: Boolean = false,
    @SerializedName("Group") var group: String = "",
    @SerializedName("Selected") var selected: Boolean = false,
    @SerializedName("Text") var text: String = "",
    @SerializedName("Value") var value: String = ""
)


