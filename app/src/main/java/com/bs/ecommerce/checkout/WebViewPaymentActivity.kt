package com.bs.ecommerce.checkout

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.activity_payment_toolbar.*
import kotlinx.android.synthetic.main.fragment_payment_info.*


class WebViewPaymentActivity : BaseActivity()
{
    var nextStep = 0

    override fun getLayoutId(): Int = R.layout.activity_webview_payment

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        intent?.extras?.let {

            when(it.getInt(CheckoutConstants.CHECKOUT_STEP))
            {
                CheckoutConstants.PaymentInfo ->
                {
                    toolbarTitle?.text = it.getString(CheckoutConstants.PAYMENT_INFO_NAME)
                    setWebView(CheckoutConstants.PaymentInfoUrl)
                }
                CheckoutConstants.RedirectToGateway ->
                {
                    toolbarTitle?.text = "Online Payment"
                    setWebView(CheckoutConstants.RedirectUrl)
                }
            }
        }



        back_arrow?.setOnClickListener {

            if(paymentInfoWebView?.canGoBack()!!)
                paymentInfoWebView?.goBack()
            else
                onBackPressed()

        }
    }


    override fun onBackPressed() {


        if(nextStep == CheckoutConstants.Completed)
            startActivity(Intent(this, ResultActivity::class.java)
                .putExtra(CheckoutConstants.CHECKOUT_STEP, CheckoutConstants.Completed)
                .putExtra(CheckoutConstants.ORDER_ID, "10")
            )

        else
        {
            val intent = Intent()
            intent.putExtra(CheckoutConstants.NEXT_STEP_AFTER_PAYMENT_INFO, nextStep)
            setResult(RESULT_OK, intent)

            super.onBackPressed()
        }




    }

    private fun setWebView(webViewUrl: String)
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
                "WebView".showLog("onPageStarted\t $url")

                /*if(url.contains(NetworkConstants.SITE_URL) && needToCheckUrl)
                {
                    "WebView".showLog("2nd time ${NetworkConstants.SITE_URL}. returning home")
                }*/
            }

            override fun onPageFinished(view: WebView, url: String)
            {
                "WebView".showLog("onPageFinished\t $url")

                /*if(url.contains(NetworkConstants.SITE_URL) && !needToCheckUrl)
                {
                    needToCheckUrl = true
                    "WebView".showLog("first time ${NetworkConstants.SITE_URL}. ignoring")
                }*/
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean
            {

                "WebView".showLog("shouldOverrideUrlLoading $url")

                if(url.contains("/step/"))
                {
                    nextStep = url[url.lastIndex].toString().toInt()
                    "webview_step".showLog(nextStep.toString())
                    //toast(nextStep.toString())
                    onBackPressed()
                }
                view.loadUrl(webViewUrl, NetworkUtil.headers)
                //view.postUrl(CheckoutConstants.PaymentInfoUrl, NetworkUtil.headers.entries.toString().toByteArray(charset = Charsets.UTF_8))
                return false
            }
        }
        paymentInfoWebView?.loadUrl(webViewUrl, NetworkUtil.headers)
    }

}
