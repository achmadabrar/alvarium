package com.bs.ecommerce.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bs.ecommerce.main.MainActivity


class SplashScreenActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}
