package com.bs.ecommerce.ui.home

import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout = register_root_layout

    override fun createViewModel(): BaseViewModel = MainViewModel()

}
