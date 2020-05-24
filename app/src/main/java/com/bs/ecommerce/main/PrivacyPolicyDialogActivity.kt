package com.bs.ecommerce.main

import android.util.Log
import android.webkit.WebView
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.more.model.TopicModel
import com.bs.ecommerce.more.model.TopicModelImpl
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.utils.AcceptPolicyPreference
import com.bs.ecommerce.utils.MyApplication

abstract class PrivacyPolicyDialogActivity : BaseActivity()
{
    private lateinit var prefManager: AcceptPolicyPreference

    private lateinit var model: TopicModel

    override fun onResume()
    {
        super.onResume()

        model = TopicModelImpl()

        prefManager = AcceptPolicyPreference(this)

        if (prefManager.isFirstTimeLaunch)
            (viewModel as MainViewModel).fetchTopic(Api.topicPrivacyPolicy, model)


        (viewModel as MainViewModel).apply {

            topicLD.observe(this@PrivacyPolicyDialogActivity, Observer { topic ->

                topic?.body?.also {

                    if(prefManager.isFirstTimeLaunch)
                        startPrivacyPolicyDialog(it)
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

        MaterialDialog.Builder(this)
                .title("Please Read This Before Continue")
                .positiveText("I Read & I Accept")
                .positiveColorRes(R.color.colorPrimaryDark)
                //.negativeText(R.string.disagree)
                //.checkBoxPrompt("I Understand & Accept", false, null)
                .onAny{
                    materialDialog: MaterialDialog, dialogAction: DialogAction ->

                    prefManager.isFirstTimeLaunch = false

                    if(prefManager.isInstanceIdReceived)
                    {
                        MyApplication.fcm_token?.let {

                            //sendRegistrationToServer(MyApplication.fcm_token)
                            Log.v("rahat_fcm", "Registered\t" + MyApplication.fcm_token)

                        }

                    }


                }
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .customView(webViewDialog, false)
                .show()
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