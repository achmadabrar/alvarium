package com.bs.ecommerce.auth.register.data

import com.bs.ecommerce.networking.BaseResponse
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

    @SerializedName("CompanyEnabled") var companyEnabled: Boolean = false

    @SerializedName("GenderEnabled") var genderEnabled: Boolean = false

    @SerializedName("DateOfBirthEnabled") var dateOfBirthEnabled: Boolean = false

    @SerializedName("NewsletterEnabled") var newsletterEnabled: Boolean = false

    @SerializedName("PhoneEnabled") var phoneEnabled: Boolean = false

    @SerializedName("CustomerAttributes") var customerAttributes: List<ProductAttribute>? = null

    @SerializedName("FormValues") var formValues: List<KeyValuePair>? = null

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

class ProductAttribute {
    @SerializedName("ProductId") var productId: Long = 0
    @SerializedName("Id") var id: Int = 0


    @SerializedName("ProductAttributeId") var productAttributeId: Long = 0
    @SerializedName("Name") var name: String? = null
    @SerializedName("Description") var description: String? = null
    @SerializedName("TextPrompt") var textPrompt: String? = null
    @SerializedName("IsRequired") var isRequired: Boolean = false
    @SerializedName("DefaultValue") var defaultValue: String? = null
    @SerializedName("AttributeControlType") var attributeControlType: Int = 0
    @SerializedName("Values") var values: List<AttributeControlValue> = mutableListOf()
    @SerializedName("SelectedDay") private val SelectedDay: Any? = null
    @SerializedName("SelectedMonth") private val SelectedMonth: Any? = null
    @SerializedName("SelectedYear") private val SelectedYear: Any? = null


}
class AttributeControlValue
{
    @SerializedName("Name") var name: String? = null
    @SerializedName("ColorSquaresRgb") var colorSquaresRgb: String? = null
    @SerializedName("PriceAdjustment") var priceAdjustment: String? = null
    @SerializedName("PriceAdjustmentValue") var priceAdjustmentValue: Double = 0.toDouble()
    @SerializedName("IsPreSelected") var isPreSelected: Boolean = false
    @SerializedName("Id") var id: Int = 0
    @SerializedName("DefaultValue") var defaultValue: String? = null


}

data class KeyValuePair( @SerializedName("Key") var key: String = "",
                         @SerializedName("Value") var value: String = "")