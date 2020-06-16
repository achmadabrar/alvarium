package com.bs.ecommerce.utils

import android.content.Context
import android.content.SharedPreferences


class AcceptPolicyPreference(context: Context)
{
    val pref: SharedPreferences
    val editor: SharedPreferences.Editor

    var PRIVATE_MODE = 0

    var isNotAccepted: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)

        set(isFirstTime)
        {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    var fcmTokenChanged: Boolean
        get() = pref.getBoolean(IS_INSTANCE_ID_RECEIVED, false)

        set(idReceived)
        {
            editor.putBoolean(IS_INSTANCE_ID_RECEIVED, idReceived)
            editor.commit()
        }



    var isUserNotRegistered: Boolean
        get() = pref.getBoolean(IS_USER_NOT_REGISTERED, true)

        set(isFirstTime)
        {
            editor.putBoolean(IS_USER_NOT_REGISTERED, isFirstTime)
            editor.commit()
        }


    init
    {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object
    {
        private val PREF_NAME = "nopstation_preference"

        private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

        private val IS_USER_NOT_REGISTERED = "IS_USER_NOT_REGISTERED"

        private val IS_INSTANCE_ID_RECEIVED = "IS_INSTANCE_ID_RECEIVED"
    }

}
