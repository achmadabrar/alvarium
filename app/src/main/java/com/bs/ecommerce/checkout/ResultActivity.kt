package com.bs.ecommerce.checkout

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.activity_payment_toolbar.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.fragment_payment_info.*
import java.nio.charset.Charset


class ResultActivity : BaseActivity()
{

    override fun getLayoutId(): Int = R.layout.activity_result

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        intent?.extras?.let {

            if(it.getInt(CheckoutConstants.CHECKOUT_STEP) == CheckoutConstants.Completed)
            {
                toolbarTitle.text =  "${getString(R.string.your_order_is_confirm)}"

                resultText.text = "${getString(R.string.order_number_is)} ${it.getInt(CheckoutConstants.ORDER_ID)}"

                resultButton.text = "${getString(R.string.ok)}"
            }
        }


        resultButton?.setOnClickListener {  goToMainActivity() }

        back_arrow?.setOnClickListener {    goToMainActivity() }
    }

    private fun goToMainActivity()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}
