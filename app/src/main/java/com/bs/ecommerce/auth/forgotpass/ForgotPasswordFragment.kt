package com.bs.ecommerce.auth.forgotpass

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.utils.isEmailValid
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var model: AuthModel

    override fun getFragmentTitle() = R.string.title_forgot_password

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun getRootLayout(): RelativeLayout? = forgotPasswordRootLayout

    override fun createViewModel(): BaseViewModel = PasswordViewModel()

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
        btnSendEmail.setOnClickListener {

            val email = etEmail.text.toString().trim()

            if (email.isEmailValid()) {
                (viewModel as PasswordViewModel).requestForgetPassword(email, model)
            } else {
                val toastMsg =
                    "${etEmail.hint} ${requireContext().getString(R.string.reg_hint_is_required)}"

                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
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