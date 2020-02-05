package com.bs.ecommerce.networking

import java.util.*

/**
 * Created by bs156 on 09-Dec-16.
 */

object NetworkUtil
{
    private var deviceId = ""
    var token: String = ""
    internal var nst = ""
        set

    val headers: Map<String, String>
        get()
        {
            val headerMap = HashMap<String, String>()

            if (deviceId.isEmpty())
                deviceId = DeviceId.get()

            if(token.isNotEmpty())
                headerMap["Token"] = token

            headerMap["DeviceId"] = deviceId
            headerMap["NST"] = nst

            return headerMap
        }

    fun getDeviceId(): String
    {
        if (deviceId.isEmpty())
            deviceId = DeviceId.get()

        return deviceId
    }
}
