package com.bs.ecommerce.base

import android.view.View
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainActivity
import kotlinx.android.synthetic.main.toolbar.*


abstract class ToolbarLogoBaseFragment : BaseFragment()
{
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
        //(activity as MainActivity).toolbarTop.setLogo(R.drawable.ic_nopstation_logo)
        (activity as MainActivity).topLogoLayout?.visibility = View.VISIBLE
    }

}