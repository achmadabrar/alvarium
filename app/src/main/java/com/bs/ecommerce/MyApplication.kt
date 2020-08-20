package com.bs.ecommerce

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.checkout.model.data.CheckoutSaveResponse
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
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

        mAppContext = this.applicationContext
    }


    companion object {
        var appStartCalled = false
        var navigateFromCheckoutCompleteToHomePage = false

        var myCartCounter: Int = 0

        var mAppContext: Context? = null

        fun setCartCounter(counter: Int)
        {
            myCartCounter = counter
        }

        var checkoutSaveResponse : CheckoutSaveResponse? = CheckoutSaveResponse()
        var getBillingResponse : BillingAddressResponse? = BillingAddressResponse()

        var previouslySelectedTab = 0
        var logoUrl = ""

        var isJwtActive = false
        var isAnonymousCheckoutAllowed = false

        var billingNewAddressSaved = false
    }

}
