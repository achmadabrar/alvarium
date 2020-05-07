package com.bs.ecommerce.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bs.ecommerce.checkout.model.data.SaveBillingResponse
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
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

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)


        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        mAppContext = this.applicationContext
    }


    companion object {

        var fcm_token : String? = null

        var searchQuery = ""
        var paypalKey = "payPalKey"
        var myCartCounter: Int = 0
        val orderIdKey = "orderIdKey"

        var mAppContext: Context? = null

        fun setCartCounter(counter: Int)
        {
            myCartCounter = counter
            //BaseActivity.updateHotCount(counter)
        }

        var saveBillingResponse : SaveBillingResponse? = null
    }

}
