package com.bs.ecommerce.main.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 3/2/2016.
 */
data class AppStartRequest(

    @SerializedName("DeviceTypeId") var deviceTypeId: Int = 0,
    @SerializedName("SubscriptionId") var subscriptionId: String? = null,
    @SerializedName("EmailAddress") var emailAddress: String? = null
)
