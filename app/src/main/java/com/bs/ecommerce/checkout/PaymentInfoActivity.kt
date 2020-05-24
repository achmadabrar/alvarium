package com.bs.ecommerce.checkout

import android.R.attr.data
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
import kotlinx.android.synthetic.main.fragment_payment_info.*


class PaymentInfoActivity : BaseActivity()
{
    override fun getLayoutId(): Int = R.layout.activity_payment_info

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setWebView()

        back_arrow?.setOnClickListener {

            if(paymentInfoWebView?.canGoBack()!!)
                paymentInfoWebView?.goBack()
            else
                onBackPressed()

        }
    }


    override fun onBackPressed() {

        val intent = Intent()
        intent.putExtra(CheckoutConstants.NEXT_STEP_AFTER_PAYMENT_INFO, CheckoutConstants.ConfirmOrder)
        setResult(RESULT_OK, intent)

        super.onBackPressed()

    }

    private fun setWebView()
    {
        paymentInfoWebView?.settings?.javaScriptEnabled = true
        paymentInfoWebView?.settings?.domStorageEnabled = true
        paymentInfoWebView?.settings?.builtInZoomControls = true
        paymentInfoWebView?.settings?.displayZoomControls = false

        paymentInfoWebView?.webViewClient = object : WebViewClient()
        {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?)
            {
                super.onPageStarted(view, url, favicon)
                "WebView".showLog("onPageStarted\tyour current url when webpage loading..$url")

                /*if(url.contains(NetworkConstants.SITE_URL) && needToCheckUrl)
                {
                    "WebView".showLog("2nd time ${NetworkConstants.SITE_URL}. returning home")
                }*/
            }

            override fun onPageFinished(view: WebView, url: String)
            {
                "WebView".showLog("onPageFinished\tyour current url when webpage loading.. finish$url")

                /*if(url.contains(NetworkConstants.SITE_URL) && !needToCheckUrl)
                {
                    needToCheckUrl = true
                    "WebView".showLog("first time ${NetworkConstants.SITE_URL}. ignoring")
                }*/
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean
            {
                view.loadUrl(CheckoutConstants.PaymentInfoUrl, NetworkUtil.headers)
                return false
            }
        }
        paymentInfoWebView?.loadUrl(CheckoutConstants.PaymentInfoUrl, NetworkUtil.headers)
    }

}
