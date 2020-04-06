package com.bs.ecommerce.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.binjar.prefsdroid.Preference
import com.bs.ecommerce.base.BaseActivity
import com.pixplicity.easyprefs.library.Prefs


class MyApplication : MultiDexApplication()
{

    override fun attachBaseContext(base: Context)
    {
        super.attachBaseContext(base)
        MultiDex.install(this) // install multidex
    }

    override fun onCreate() {
        super.onCreate()


        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()


    }


    companion object {

        var fcm_token : String? = null

        var searchQuery = ""
        var paypalKey = "payPalKey"
        var myCartCounter: Int = 0
        val orderIdKey = "orderIdKey"

        fun setCartCounter(counter: Int)
        {
            myCartCounter = counter
            //BaseActivity.updateHotCount(counter)
        }
    }

}
