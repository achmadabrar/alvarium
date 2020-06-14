package com.bs.ecommerce.main.model.data

import com.google.gson.annotations.SerializedName

data class AppStartRequest(
    @SerializedName("Data") var appStartData: AppStartData?
)

data class AppStartData(
    @SerializedName("DeviceTypeId") var deviceTypeId: Int,
    @SerializedName("SubscriptionId") var subscriptionId: String?
)