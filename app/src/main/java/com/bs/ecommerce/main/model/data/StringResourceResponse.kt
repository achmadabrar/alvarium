package com.bs.ecommerce.main.model.data


import com.bs.ecommerce.main.model.data.StringResource
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class StringResourceResponse(
    @SerializedName("Data")
    val stringResource: List<StringResource>
): BaseResponse()