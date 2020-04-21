package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName

class AddToCartResponse(@SerializedName("Success") val isSuccess: Boolean,
                        @SerializedName("ForceRedirect") val isForceRedirect: Boolean,
                        @SerializedName("Count") val count: Int)
    : BaseResponse()