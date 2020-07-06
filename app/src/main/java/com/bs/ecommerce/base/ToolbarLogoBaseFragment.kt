package com.bs.ecommerce.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*


abstract class ToolbarLogoBaseFragment : BaseFragment()
{

    var logoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        logoUrl = mainViewModel.appSettingsLD.value?.peekContent()?.logoUrl ?: MyApplication.logoUrl
        MyApplication.logoUrl = logoUrl
    }

    override fun onStop() {
        super.onStop()
        hideTopLogo()
    }

    override fun onResume() {
        super.onResume()
        showTopLogo()
    }


    protected fun hideTopLogo() {
        //(activity as MainActivity).toolbarTop.logo = null
        (activity as MainActivity).topLogoLayout?.visibility = View.GONE
    }

    protected fun showTopLogo() {
        activity?.title = ""
        (activity as MainActivity).topLogoLayout?.visibility = View.VISIBLE
        (activity as MainActivity).topLogoLayout?.topLogo?.loadImg(logoUrl, null)
    }

}