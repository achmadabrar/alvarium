package com.bs.ecommerce.networking.common

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.google.gson.annotations.SerializedName


data class KeyValueFormData(@SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf())


data class ExistingAddress(
    @SerializedName("Data") var data: Data = Data(),
    @SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf()
)

data class Data(@SerializedName("ShipToSameAddress") var shipToSameAddress: Boolean = false,
                @SerializedName("PickupInStore") var pickupInStore: Boolean = false)

