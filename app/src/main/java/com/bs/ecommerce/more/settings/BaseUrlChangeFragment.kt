package com.bs.ecommerce.more.settings


import android.content.Intent
import android.text.Selection
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.hideKeyboard
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_settings.*


abstract class BaseUrlChangeFragment : BaseFragment()
{

    lateinit var mainModel: MainModel
    lateinit var mainViewModel: MainViewModel

    abstract override fun getFragmentTitle(): String

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun getRootLayout(): RelativeLayout? = settingsFragmentRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    protected fun keepOldUrl()
    {
        NetworkConstants.BASE_URL = NetworkConstants.DEFAULT_URL
        prefObject.setPrefs(PrefSingleton.SHOULD_USE_NEW_URL, false)
        forceRunApp()
    }

    protected fun validateForm()
    {
        if(newBaseUrlEditTextFromSettings?.text?.length ?: 0 > 10)
            testApiCall()
        else
            toast(DbHelper.getString(Const.SETTINGS_INVALID_URL))
            //newBaseUrlEditTextFromSettings?.error = getString(R.string.url_is_required)

    }

    private fun testApiCall()
    {
        NetworkConstants.BASE_URL = newBaseUrlEditTextFromSettings?.text?.trim().toString() + "/api/"


        mainViewModel.getNavDrawerCategoryList(mainModel)

        mainViewModel.testUrlSuccessLD.observe(viewLifecycleOwner, Observer { success ->

            success?.let {
                if (it == "--") {
                    changeBaseUrl()
                    forceRunApp()
                } else {
                    toast(
                        if (it.isNotEmpty())
                            it
                        else DbHelper.getString(Const.SETTINGS_BASE_URL_CHANGE_FAIL)
                    )
                    NetworkConstants.BASE_URL = NetworkConstants.DEFAULT_URL
                }
            }
            mainViewModel.testUrlSuccessLD.removeObservers(this)
        })


        mainViewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

            showHideLoader(isShowLoader)
            mainViewModel.isLoadingLD.removeObservers(this)
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

        NetworkConstants.BASE_URL = url
        prefObject.setPrefs(PrefSingleton.NEW_URL, url)
        prefObject.setPrefs(PrefSingleton.SHOULD_USE_NEW_URL, true)

    }

    private fun forceRunApp()
    {
        performLogout()

        DbHelper.memCache = mainViewModel.appSettingsLD.value?.peekContent()

        requireActivity().finish()

        val intent = Intent(requireActivity(), MainActivity::class.java)
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
