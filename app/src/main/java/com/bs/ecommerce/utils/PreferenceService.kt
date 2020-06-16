package com.bs.ecommerce.utils


import com.bs.ecommerce.auth.register.data.CustomerInfo
import com.bs.ecommerce.networking.Api
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs


object PrefSingleton
{

    fun getPrefs(key: String): String = Prefs.getString(key, "")


    fun getPrefsIntValue(key: String): Int = Prefs.getInt(key, -1)


    fun getPrefsBoolValue(key: String): Boolean = Prefs.getBoolean(key, false)

    fun setPrefs(key: String, value: String)
    {
        Prefs.putString(key, value)
    }

    fun setPrefs(key: String, value: Boolean?)
    {
        Prefs.putBoolean(key, value!!)
    }

    fun setPrefs(key: String, value: Int)
    {
        Prefs.putInt(key, value)
    }

    fun getPageSize() : Int {
        //return Prefs.contains(PAGE_SIZE)
        return   Prefs.getInt(PAGE_SIZE, Api.DEFAULT_PAGE_SIZE)
    }

    fun setPageSize(str: String?) {
        if(!str.isNullOrEmpty() && str.isNumeric())
            Prefs.putInt(PAGE_SIZE, str.toInt())
        else
            Prefs.putInt(PAGE_SIZE, Api.DEFAULT_PAGE_SIZE)
    }

    fun setCustomerInfo(key: String, value: CustomerInfo?) {
        Prefs.putString(key, if(value==null) "" else Gson().toJson(value))
    }

    fun getCustomerInfo(key: String) : CustomerInfo? {
        val v =   Prefs.getString(key, "")

        return if(v.isNotEmpty()) {
            try {
                Gson().fromJson(v, CustomerInfo::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    object PrefSingleton


    val FIRST_RUN = "firstRun"
    var SHARED_PREF_KEY = "PREFER_KEY"
    var loggedInText = "isLoggedIn"
    var IS_LOGGED_IN = "isLoggedIn"
    var TOKEN_KEY = "token"
    var NEW_URL = "URL_CHANGE_KEY"
    var SHOULD_USE_NEW_URL = "URL_CHANGE_KEY_BOOLEAN"
    var APP_VERSION_CODE_KEY = "VERSION_CODE"

    var SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER_KEY"
    var FCM_TOKEN = "FCM_TOKEN"

    var REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE_KEY"
    var taxShow = "taxShow"
    var discuntShow = "discuntShow"
    var CURRENT_LANGUAGE = "current_language"
    var CURRENT_CURRENCY = "CURRENT_CURRENCY"
    var IS_DARK_THEME = "IS_DARK_THEME"
    var CURRENT_LANGUAGE_ID = "current_language_id"
    var NST = "nst"
    var DeviceID = "DEVICE_UNIQUE_ID"
    var CUSTOMER_INFO = "CUSTOMER_INFO"
    var PAGE_SIZE = "PAGE_SIZE"

}
