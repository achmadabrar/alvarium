package com.bs.ecommerce.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bs.ecommerce.R
import com.pixplicity.easyprefs.library.Prefs
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


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

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("montserrat_regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
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
