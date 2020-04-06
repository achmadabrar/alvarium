package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_confirm_order.*

class ConfirmOrderFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_confirm_order

    override fun getRootLayout(): RelativeLayout = confirmOrderRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        allLayoutExceptButton.visibility = View.VISIBLE
        shippingLayout.visibility = View.VISIBLE
        checkoutButton.visibility = View.VISIBLE
    }
}