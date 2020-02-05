package com.bs.ecommerce.networking

import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.Streams

/**
 * Created by bs-110 on 12/9/2015.
 */
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

/*    @SerializedName("CustomerAttributes") var customerAttributes: List<ProductAttribute>? = null

    @SerializedName("FormValues") var formValues: List<KeyValuePair>? = null*/

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
