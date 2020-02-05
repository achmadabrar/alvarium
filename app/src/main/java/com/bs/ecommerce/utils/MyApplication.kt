package com.bs.ecommerce.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.binjar.prefsdroid.Preference
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

/*        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)*/

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        //Preference.load().using(this).with(PrefSingleton.SHARED_PREF_KEY).prepare()
    }


    companion object {

        var fcm_token : String? = null
    }

}
