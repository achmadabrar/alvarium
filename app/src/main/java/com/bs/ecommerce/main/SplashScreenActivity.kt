package com.bs.ecommerce.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bs.ecommerce.R
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.utils.PrefSingleton
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.UnsupportedEncodingException
import java.util.*


class SplashScreenActivity : BaseActivity()
{

    private val NST_KEY = "bm9wU3RhdGlvblRva2Vu"
    private val NST_SECRET = "bm9wS2V5"


    override fun getLayoutId(): Int = R.layout.activity_splash_screen

    override fun createViewModel(): BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initializeData()
    }


    private fun startMainActivity()
    {
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
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

    private fun startThreadAndGoMainActivity()
    {
        val handler = Handler()
        val t = Timer()
        t.schedule(object : TimerTask()
        {
            override fun run() {
                handler.postDelayed({ startMainActivity() }, 3000)
            }
        }, 0)
    }

}
