package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 12/8/2015.
 */
data class StateListResponse(@SerializedName("Data") val stateList: List<AvailableState>?) : BaseResponse()


class AvailableState(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    override fun toString(): String {
        return name
    }
}