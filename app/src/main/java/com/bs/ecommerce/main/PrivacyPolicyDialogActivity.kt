package com.bs.ecommerce.main

import android.webkit.WebView
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.AppStartData
import com.bs.ecommerce.more.topic.model.TopicModel
import com.bs.ecommerce.more.topic.model.TopicModelImpl
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.utils.*

abstract class PrivacyPolicyDialogActivity : BaseActivity()
{

    protected lateinit var mainModel: MainModelImpl
    protected lateinit var mainViewModel: MainViewModel

    private lateinit var prefManager: AcceptPolicyPreference

    private lateinit var model: TopicModel

    private var isDialogAlreadyShowed = false


    private fun checkPrivacyPolicyAndSendAppStartToken()
    {
        model = TopicModelImpl()

        if (prefManager.isNotAccepted && !isDialogAlreadyShowed)
            (viewModel as MainViewModel).fetchTopic(Api.topicPrivacyPolicy, model)


        (viewModel as MainViewModel).apply {

            topicLD.observe(this@PrivacyPolicyDialogActivity, Observer { topic ->

                topic?.body?.also {

                    if(!isDialogAlreadyShowed)
                    {
                        startPrivacyPolicyDialog(it)
                        isDialogAlreadyShowed = true
                    }
                }
            })
        }
    }

    override fun onResume()
    {
        super.onResume()

        prefManager = AcceptPolicyPreference(this)

        if (prefManager.isNotAccepted && !isDialogAlreadyShowed)
            checkPrivacyPolicyAndSendAppStartToken()
        
        else if(prefManager.fcmTokenChanged)
            sendAppStartDataToServer()
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

                sendAppStartDataToServer()
            }
            cancelable(false)
            cancelOnTouchOutside(false)
        }
    }


    private fun sendAppStartDataToServer()
    {
        val fcmToken = PrefSingleton.getPrefs(PrefSingleton.FCM_TOKEN)
        TAG.showLog("Sending Token\t$fcmToken")

        val appStartData = AppStartData(deviceTypeId = 10, subscriptionId = fcmToken)

        (viewModel as MainViewModel).submitAppStart(appStartData, mainModel)

        mainViewModel.appStartResponseLD.observe(this, Observer { appStartResponse ->

            PrefSingleton.setPrefs(PrefSingleton.SENT_TOKEN_TO_SERVER, appStartResponse)
            TAG.showLog("Token Sent Successful")
            prefManager.fcmTokenChanged = false
        })
    }

    companion object
    {
        private val TAG = "fcm"
    }
}