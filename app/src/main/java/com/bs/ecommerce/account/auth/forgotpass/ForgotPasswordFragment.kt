package com.bs.ecommerce.account.auth.forgotpass

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.AuthModel
import com.bs.ecommerce.account.auth.AuthModelImpl
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.isEmailValid
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var model: AuthModel

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_FORGOT_PASS)

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun getRootLayout(): RelativeLayout? = forgotPasswordRootLayout

    override fun createViewModel(): BaseViewModel =
        PasswordViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = AuthModelImpl()
            viewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)

            setupView()
        }

        setLiveDataObserver()
    }

    private fun setupView() {
        tilEmail.hint = DbHelper.getString(Const.FORGOT_PASS_EMAIL)
        btnSendEmail.text = DbHelper.getString(Const.FORGOT_PASS_BTN)

        btnSendEmail.setOnClickListener {

            val email = etEmail.text.toString().trim()

            if (email.isEmailValid()) {
                (viewModel as PasswordViewModel).requestForgetPassword(email, model)
            } else {
                toast(DbHelper.getString(Const.FORGOT_PASS_EMAIL_REQ))
            }
        }
    }

    private fun setLiveDataObserver() {

        (viewModel as PasswordViewModel).apply {

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            actionSuccessLD.observe(viewLifecycleOwner, Observer { actionSuccess ->
                if (actionSuccess)
                    etEmail?.text?.clear()
            })
        }
    }
}