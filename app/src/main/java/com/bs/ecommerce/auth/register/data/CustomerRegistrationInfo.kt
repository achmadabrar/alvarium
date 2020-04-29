package com.bs.ecommerce.auth.register.data

import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.product.model.data.ProductAttribute
import com.google.gson.annotations.SerializedName

class CustomerRegistrationInfo : CustomerInfo() {
    @SerializedName("Password") var password: String? = null
    @SerializedName("ConfirmPassword") var confirmPassword: String? = null

    @SerializedName("FormValue") var formValue: List<KeyValuePair>? = null
}
class FormValue(@SerializedName("key") var key : String, @SerializedName("value") var value : String)







open class CustomerInfo : BaseResponse() {
    @SerializedName("FirstName") var firstName: String? = null
    @SerializedName("LastName") var lastName: String? = null
    @SerializedName("DateOfBirthDay") var dateOfBirthDay: Int = 0
    @SerializedName("DateOfBirthMonth") var dateOfBirthMonth: Int = 0
    @SerializedName("DateOfBirthYear") var dateOfBirthYear: Int = 0
    @SerializedName("Email") var email: String? = null
    @SerializedName("Company") var company: String? = null
    @SerializedName("Newsletter") var isNewsletter: Boolean = false
    @SerializedName("Gender") var gender: String? = null
    @SerializedName("Username") var username: String? = null
    @SerializedName("UsernamesEnabled") var UsernamesEnabled: Boolean = false
    @SerializedName("Phone") var phone: String? = null

    @SerializedName("StreetAddress") var streetAddress: String = ""
    @SerializedName("StreetAddress2") var streetAddress2: String = ""
    @SerializedName("ZipPostalCode") var zipPostalCode: String = ""

    @SerializedName("City") var city: String = ""
    @SerializedName("CountryId") var countryId: Int = 0
    @SerializedName("County") var county: String = ""
    @SerializedName("StateProvinceId") var stateProvinceId: Int = 0

    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false

    @SerializedName("GenderEnabled") var genderEnabled: Boolean = false

    @SerializedName("DateOfBirthEnabled") var dateOfBirthEnabled: Boolean = false

    @SerializedName("NewsletterEnabled") var newsletterEnabled: Boolean = false

    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false

    @SerializedName("CustomerAttributes") var customerAttributes: List<ProductAttribute>? = null


    override fun toString(): String {
        return "CustomerInfo{" +
                "FirstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", DateOfBirthDay=" + dateOfBirthDay +
                ", DateOfBirthMonth=" + dateOfBirthMonth +
                ", DateOfBirthYear=" + dateOfBirthYear +
                ", Email='" + email + '\'' +
                ", Company='" + company + '\'' +
                ", Newsletter=" + isNewsletter +
                ", Gender='" + gender + '\'' +
                '}'
    }


}

data class KeyValuePair( @SerializedName("Key") var key: String = "",
                         @SerializedName("Value") var value: String = "")