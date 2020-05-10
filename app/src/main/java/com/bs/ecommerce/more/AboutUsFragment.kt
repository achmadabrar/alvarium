package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_privacy_policy.*


class AboutUsFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_about_us

    override fun getLayoutId(): Int = R.layout.fragment_privacy_policy

    override fun getRootLayout(): RelativeLayout? = privacyPolicyLayout

    override fun createViewModel(): BaseViewModel = BaseViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            setupView()
        }
    }

    private fun setupView() {
        showLoading()

        wv_privacy_policy?.apply {

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    "nop_".showLog("onPageFinished")
                    hideLoading()
                }
            }

            loadUrl(
                "https://stackoverflow.com/questions/40726438/android-detect-when-the-last-item-in-a-recyclerview-is-visible"
            )
        }
    }
}