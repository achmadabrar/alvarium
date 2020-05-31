package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class StringResourceResponse(
    @SerializedName("Data")
    val stringResource: List<StringResource>
): BaseResponse()