package com.bs.ecommerce.account.orders.model.data


import com.google.gson.annotations.SerializedName

data class ReturnReqFormResponse(
    @SerializedName("Data")
    val `data`: ReturnReqFormData?,
    @SerializedName("ErrorList")
    val errorList: List<Any>?,
    @SerializedName("Message")
    val message: Any?
)