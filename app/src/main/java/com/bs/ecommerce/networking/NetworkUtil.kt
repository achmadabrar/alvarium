package com.bs.ecommerce.networking

import com.bs.ecommerce.BuildConfig
import com.bs.ecommerce.MyApplication
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.UnsupportedEncodingException
import java.util.*

/**
 * Created by bs156 on 09-Dec-16.
 */

object NetworkUtil
{
    private var deviceId = ""
    var token: String = ""
    var nst = ""


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

    private fun dateToUTC(date: Date): Date
            = Date(date.time - Calendar.getInstance().timeZone.getOffset(date.time))

    fun getSecurityToken() : String?
    {

        val NST_KEY = BuildConfig.NST_KEY
        val NST_SECRET = BuildConfig.NST_SECRET

        var compactJws: String? = null

        val createdDate = Date()

        try {
            compactJws = Jwts.builder()
                .claim("NST_KEY", NST_KEY)
                .setIssuedAt(createdDate)
                .signWith(SignatureAlgorithm.HS512, NST_SECRET.toByteArray(charset("UTF-8")))
                .compact()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        MyApplication.isJwtActive = true

        return compactJws
    }
}
