package com.bs.ecommerce.utils

import com.binjar.prefsdroid.Preference
import com.pixplicity.easyprefs.library.Prefs

/**
 * Created by bs206 on 3/15/18.
 */
object PrefSingleton
{

    fun getPrefs(key: String): String = Prefs.getString(key, "")


    fun getPrefsIntValue(key: String): Int = Prefs.getInt(key, -1)


    fun getPrefsBoolValue(key: String): Boolean? = Prefs.getBoolean(key, false)

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

    object PrefSingleton


    val FIRST_RUN = "firstRun"
    var SHARED_PREF_KEY = "PREFER_KEY"
    var loggedInText = "isLoggedIn"
    var LOGGED_PREFER_KEY = "isLoggedIn"
    var TOKEN_KEY = "token"
    var URL_PREFER_KEY = "URL_CHANGE_KEY"
    var DO_USE_NEW_URL = "URL_CHANGE_KEY_BOOLEAN"
    var APP_VERSION_CODE_KEY = "VERSION_CODE"
    var SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER_KEY"
    var REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE_KEY"
    var taxShow = "taxShow"
    var discuntShow = "discuntShow"
    var CURRENT_LANGUAGE = "current_language"
    var CURRENT_CURRENCY = "CURRENT_CURRENCY"
    var CURRENT_LANGUAGE_ID = "current_language_id"
    var NST = "nst"
    var DeviceID = "DEVICE_UNIQUE_ID"


/*        lateinit var service: PrefSingleton


        val instance: PrefSingleton
            get()
            {
                if (service == null)
                {
                    service = PrefSingleton()

                    println("isInitialized before assignment:  $this::service.isInitialized")
                }
                return service
            }*/



}
