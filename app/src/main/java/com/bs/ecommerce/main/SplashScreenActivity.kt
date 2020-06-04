package com.bs.ecommerce.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.BuildConfig
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.showLog
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.UnsupportedEncodingException


class SplashScreenActivity : BaseActivity()
{

/*    private val NST_KEY = "bm9wU3RhdGlvblRva2Vu"
    private val NST_SECRET = "bm9wS2V5"*/


    private val NST_KEY = BuildConfig.NST_KEY
    private val NST_SECRET = BuildConfig.NST_SECRET

    override fun getLayoutId(): Int = R.layout.activity_splash_screen

    override fun createViewModel(): BaseViewModel? = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        observeLiveData()
        initializeData()
    }


    private fun observeLiveData() {

        (viewModel as MainViewModel).apply {

            appSettingsLD.observe(this@SplashScreenActivity, Observer { appSettings ->

                appSettings.getContentIfNotHandled()?.let {

                    if(it.stringResources.isNullOrEmpty()) {
                        "lang_".showLog("Download success: false")
                        finish()
                    } else {
                        "lang_".showLog("Download success: true")

                        /*if(it.andriodForceUpdate == true && !it.playStoreUrl.isNullOrEmpty()) {
                            // TODO handle force update here
                        } else {
                            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                            finish()
                        }*/

                        // removing language resources before adding to intent
                        // we don't need this huge data on MainActivity
                        it.stringResources = listOf()

                        val mainActivityIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        mainActivityIntent.putExtra(MainActivity.KEY_APP_SETTINGS, it)

                        startActivity(mainActivityIntent)
                        finish()
                    }
                }
            })
        }
    }


    private fun initializeData()
    {
        var compactJws: String? = null
        try
        {
            compactJws = Jwts.builder()
                .claim("NST_KEY", NST_KEY)
                .signWith(SignatureAlgorithm.HS512, NST_SECRET.toByteArray(charset("UTF-8"))).compact()
        }
        catch (e: UnsupportedEncodingException)
        {
            e.printStackTrace()
        }



        prefObject.setPrefs(PrefSingleton.NST, compactJws!!)
        NetworkUtil.nst = compactJws

        (viewModel as MainViewModel).getAppSettings(MainModelImpl(applicationContext), true)
    }

}
