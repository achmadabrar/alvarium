package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ContactUsData(
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Enquiry")
    val enquiry: String?,

    @SerializedName("Subject")
    val subject: String? = null,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = null,
    @SerializedName("DisplayCaptcha")
    val displayCaptcha: Boolean? = false,
    @SerializedName("Result")
    val result: Any? = null,
    @SerializedName("SubjectEnabled")
    val subjectEnabled: Boolean? = false,
    @SerializedName("SuccessfullySent")
    val successfullySent: Boolean? = false
)