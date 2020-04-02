package com.bs.ecommerce.ui.home

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.ui.base.BaseFragment
import com.bs.ecommerce.ui.base.BaseViewModel
import kotlinx.android.synthetic.main.category_left.*


class HomeFragment : BaseFragment()
{

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout = categoryRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


    }



}