package com.bs.ecommerce.more.viewmodel


import android.content.Intent
import android.text.Selection
import android.widget.RelativeLayout
import androidx.lifecycle.Observer

import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.networking.Constants

import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*


open class BaseUrlChangeFragment : BaseFragment()
{

    lateinit var mainModel: MainModel
    lateinit var mainViewModel: MainViewModel

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun getRootLayout(): RelativeLayout? = settingsFragmentRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    protected fun keepOldUrl()
    {
        Constants.BASE_URL = Constants.DEFAULT_URL
        prefObject.setPrefs(PrefSingleton.SHOULD_USE_NEW_URL, false)
        forceRunApp()
    }

    protected fun validateForm()
    {
        if(newBaseUrlEditTextFromSettings?.text?.length!! > 10)
            testApiCall()
        else
            toast(getString(R.string.url_is_required))
            //newBaseUrlEditTextFromSettings?.error = getString(R.string.url_is_required)

    }

    private fun testApiCall()
    {
        Constants.BASE_URL = newBaseUrlEditTextFromSettings?.text?.trim().toString() + "/api/"


        mainViewModel.getNavDrawerCategoryList(mainModel)

        mainViewModel.testUrlSuccessLD.observe(viewLifecycleOwner, Observer { success ->

            if(success)
            {
                changeBaseUrl()
                toast("Base URL changed successfully")
                forceRunApp()
            }
            else
                toast("Your API is not compatible with us yet")

        })


        mainViewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }

    override fun onDestroy()
    {
        super.onDestroy()
        activity?.hideKeyboard()
    }

    private fun changeBaseUrl()
    {
        val url = newBaseUrlEditTextFromSettings?.text?.trim().toString() + "/api/"

        Constants.BASE_URL = url
        prefObject.setPrefs(PrefSingleton.NEW_URL, url)
        prefObject.setPrefs(PrefSingleton.SHOULD_USE_NEW_URL, true)

    }

    private fun forceRunApp()
    {
        NetworkUtil.token = ""
        prefObject.setPrefs(PrefSingleton.IS_LOGGED_IN, false)

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("EXIT", true)
        startActivity(intent)
    }

    protected fun textChangeListener()
    {
        newBaseUrlEditTextFromSettings.setText("https://")
        Selection.setSelection(newBaseUrlEditTextFromSettings.text, newBaseUrlEditTextFromSettings.text.length)
    }



}
