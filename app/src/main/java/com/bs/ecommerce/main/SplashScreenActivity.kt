package com.bs.ecommerce.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.BuildConfig
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.GenericErrorFragment
import com.bs.ecommerce.more.UpdateAppFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.Utils
import com.bs.ecommerce.utils.showLog
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.UnsupportedEncodingException
import java.util.*


class SplashScreenActivity : BaseActivity() {

/*    private val NST_KEY = "bm9wU3RhdGlvblRva2Vu"
    private val NST_SECRET = "bm9wS2V5"*/


    private val NST_KEY = BuildConfig.NST_KEY
    private val NST_SECRET = BuildConfig.NST_SECRET

    override fun getLayoutId(): Int = R.layout.activity_splash_screen

    override fun createViewModel(): BaseViewModel? = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeJwt()

        NetworkUtil.token = prefObject.getPrefs(prefObject.TOKEN_KEY)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        observeLiveData()

        if(Utils().isWifiOrMobileNetworkAvailable(applicationContext))
            loadAppLandingData()
    }

    fun loadAppLandingData() {
        (viewModel as MainViewModel).getAppSettings(MainModelImpl(applicationContext), true)
    }

    private fun observeLiveData() {

        (viewModel as MainViewModel).apply {

            appSettingsLD.observe(this@SplashScreenActivity, Observer { appSettings ->

                appSettings.getContentIfNotHandled().let { it ->

                    if (it == null || it.stringResources.isNullOrEmpty()) {
                        "lang_".showLog("Download success: false")

                        if(Utils().isWifiOrMobileNetworkAvailable(applicationContext)) {
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentHolder, GenericErrorFragment())
                                .commit()
                        } else {
                            "lang_".showLog("No internet connection")
                        }
                    }
                    else
                    {
                        "lang_".showLog("Download success: true")

                        MyApplication.isAnonymousCheckoutAllowed = it.anonymousCheckoutAllowed
                        PrefSingleton.setPrefs(PrefSingleton.APP_LOGO, it.logoUrl ?: "")

                        val updateNeeded =
                            (viewModel as MainViewModel).isUpdateNeeded(applicationContext, it)

                        if (updateNeeded)
                        {
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentHolder, UpdateAppFragment.newInstance(it.playStoreUrl ?: ""))
                                .commit()

                        } else {
                            DbHelper.memCache = it

                            val mainActivityIntent =
                                Intent(this@SplashScreenActivity, MainActivity::class.java)

                            intent.extras?.let {
                                mainActivityIntent.putExtras(it)
                            }

                            startActivity(mainActivityIntent)
                            finish()
                        }
                    }
                }
            })
        }
    }



    // unused from Retrofit
    private fun initializeJwt()
    {
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

        prefObject.setPrefs(PrefSingleton.NST, compactJws ?: "")
        NetworkUtil.nst = compactJws ?: ""

        MyApplication.isJwtActive = true
    }

}
