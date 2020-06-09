package com.bs.ecommerce.main

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.TopicModel
import com.bs.ecommerce.more.model.TopicModelImpl
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.utils.AcceptPolicyPreference
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.showLog

abstract class PrivacyPolicyDialogActivity : BaseActivity()
{
    private lateinit var prefManager: AcceptPolicyPreference

    private lateinit var model: TopicModel

    private var isDialogAlreadyShowed = false

    override fun onResume()
    {
        super.onResume()

        model = TopicModelImpl()

        prefManager = AcceptPolicyPreference(this)

        if (prefManager.isNotAccepted && !isDialogAlreadyShowed)
            (viewModel as MainViewModel).fetchTopic(Api.topicPrivacyPolicy, model)


        (viewModel as MainViewModel).apply {

            topicLD.observe(this@PrivacyPolicyDialogActivity, Observer { topic ->

                topic?.body?.also {

                    if(prefManager.isNotAccepted && !isDialogAlreadyShowed)
                    {
                        startPrivacyPolicyDialog(it)
                        isDialogAlreadyShowed = true
                    }
                }
            })
        }
    }


    private fun startPrivacyPolicyDialog(mHtmlString : String?)
    {

        val webViewDialog = WebView(this)
        webViewDialog.settings.defaultFontSize = 10

        webViewDialog.loadDataWithBaseURL("", mHtmlString,
                "text/html",
                "UTF-8",
                "")

        MaterialDialog(this).show {

            title(null, DbHelper.getString(Const.READ_BEFORE_CONTINUE))

            customView(null, webViewDialog, true)

            positiveButton(null, DbHelper.getString(Const.I_READ_I_ACCEPT)) {

                prefManager.isNotAccepted = false

                if(prefManager.isInstanceIdReceived)
                {
                    MyApplication.fcm_token?.let {
                        //sendRegistrationToServer(MyApplication.fcm_token)
                        "rahat_fcm".showLog("Registered\t" + MyApplication.fcm_token)
                    }

                }
            }
            cancelable(false)
            cancelOnTouchOutside(false)

        }

    }


    /*private fun sendRegistrationToServer(token: String?)
    {
        val appStartRequest = AppStartRequest()
        appStartRequest.deviceTypeId = 10
        appStartRequest.subscriptionId = token

        RetroClient.api.initApp(appStartRequest).enqueue(CustomCB<AppThemeResponse>())
    }

    @Subscribe
    fun onEvent(appInitRequestResponse: AppInitRequestResponse) {
        PrefSingleton.setPrefs(PrefSingleton.SENT_TOKEN_TO_SERVER, true)
        Log.i(TAG, "Registered")
    }*/

    companion object
    {
        private val TAG = "PrivacyPolicyActivity"
    }
}