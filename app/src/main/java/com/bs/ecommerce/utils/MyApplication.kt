package com.bs.ecommerce.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.checkout.model.data.CheckoutSaveResponse
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.ice.restring.Restring
import com.ice.restring.RestringConfig
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

        FirebaseApp.initializeApp(this)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)


        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        Restring.init(
            applicationContext,
            RestringConfig.Builder()
                .persist(true) // Set this to false to prevent saving into shared preferences.
                .stringsLoader(SampleStringsLoader())
                .build()
        )

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
        }

        var checkoutSaveResponse : CheckoutSaveResponse? = CheckoutSaveResponse()
        var getBillingResponse : BillingAddressResponse? = BillingAddressResponse()

        var previouslySelectedTab = 0
    }

}
