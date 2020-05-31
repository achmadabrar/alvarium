package com.bs.ecommerce.base

import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainActivity


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
        (activity as MainActivity).toolbarTop.logo = null
    }

    protected fun showTopLogo() {
        activity?.title = ""
        (activity as MainActivity).toolbarTop.setLogo(R.drawable.ic_nopstation_logo)
    }

}