package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 12/8/2015.
 */
data class StateListResponse(@SerializedName("Data") val data: List<AvailableCountry>?) : BaseResponse()