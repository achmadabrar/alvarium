package com.bs.ecommerce.checkout

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.data.PaymentMethod
import com.bs.ecommerce.customViews.CheckableLinearLayout
import com.bs.ecommerce.customViews.MethodSelectionProcess
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.loadImg
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_payment_info.*
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class PaymentInfoFragment : BaseCheckoutNavigationFragment() {

    override fun getFragmentTitle() = R.string.title_shopping_cart

    private var needToCheckUrl = false

    override fun getLayoutId(): Int = R.layout.fragment_payment_info

    override fun getRootLayout(): RelativeLayout = paymentInfoRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

   /* override fun onBackPressed()
    {
        if(paymentInfoWebView?.canGoBack()!!)
            paymentInfoWebView?.goBack()
        else
            super.onBackPressed()
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setWebView()


        "webview".showLog(NetworkUtil.headers.toString())
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