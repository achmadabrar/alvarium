package com.bs.ecommerce.networking

import com.bs.ecommerce.utils.PrefSingleton
import java.util.*

/**
 * Created by bs206 on 3/16/18.
 */
object DeviceId {

    private var uniqueID: String = PrefSingleton.getPrefs(PrefSingleton.DeviceID)
    @Synchronized
    fun get(): String
    {
        if (uniqueID.isEmpty())
        {
            uniqueID = UUID.randomUUID().toString()
            PrefSingleton.setPrefs(PrefSingleton.DeviceID, uniqueID)
        }


        return uniqueID
    }


}
