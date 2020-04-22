package com.bs.ecommerce.auth.forgotpass

import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_forgot_password

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun getRootLayout(): RelativeLayout? = forgotPasswordRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()
}