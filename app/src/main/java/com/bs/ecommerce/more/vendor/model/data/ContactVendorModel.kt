package com.bs.ecommerce.more.vendor.model.data


import com.google.gson.annotations.SerializedName

data class ContactVendorModel(
    @SerializedName("DisplayCaptcha")
    var displayCaptcha: Boolean?,
    @SerializedName("Email")
    var email: String?,
    @SerializedName("Enquiry")
    var enquiry: String?,
    @SerializedName("FullName")
    var fullName: String?,
    @SerializedName("Result")
    var result: Any?,
    @SerializedName("Subject")
    var subject: String?,
    @SerializedName("SubjectEnabled")
    var subjectEnabled: Boolean?,
    @SerializedName("SuccessfullySent")
    var successfullySent: Boolean?,
    @SerializedName("VendorId")
    var vendorId: Int?,
    @SerializedName("VendorName")
    var vendorName: String?
)