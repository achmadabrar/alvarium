package com.bs.ecommerce.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.BuildConfig
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
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

    override fun createViewModel(): BaseViewModel? = LanguageLoaderViewModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LanguageLoaderViewModel::class.java)

        (viewModel as LanguageLoaderViewModel).isLanguageLoaded.observe(this, androidx.lifecycle.Observer { loaded ->

            "lang_".showLog("Download success? $loaded")

            if(loaded) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            } else {
                finish()
            }
        })

        initializeData()
    }


    private fun startMainActivity()
    {
        val currentLanguageId = PrefSingleton.getPrefsIntValue(PrefSingleton.CURRENT_LANGUAGE_ID)

        if(currentLanguageId == -1 || !DbHelper.isLanguageLoaded(currentLanguageId)) {

            "lang_".showLog("No language is loaded. Downloading...")

            (viewModel as LanguageLoaderViewModel).downloadLanguage(null)
        } else {
            DbHelper.currentLanguageId = currentLanguageId
            "lang_".showLog("Language already loaded. Current language: $currentLanguageId")

            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }


    private fun initializeData()
    {
        //Picasso.with(this).load(R.drawable.splash).into(backgroundImageview)

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

        //startThreadAndGoMainActivity()        // TODO for older+common implementation
        startMainActivity()                     // TODO for cleaner implementation
    }

}
