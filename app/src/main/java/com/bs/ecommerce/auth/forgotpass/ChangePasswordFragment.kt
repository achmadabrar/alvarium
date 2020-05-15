package com.bs.ecommerce.auth.forgotpass

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.auth.login.data.ChangePasswordModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.utils.EditTextUtils
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : BaseFragment() {

    private lateinit var model: AuthModel

    override fun getFragmentTitle() = R.string.title_change_password

    override fun getLayoutId(): Int = R.layout.fragment_change_password

    override fun getRootLayout(): RelativeLayout? = forgotPasswordRootLayout

    override fun createViewModel(): BaseViewModel = PasswordViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = AuthModelImpl()
            viewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)

            (viewModel as PasswordViewModel).getChangePasswordModel(model)
        }

        setLiveDataObserver()
    }

    private fun setupView(changePasswordModel: ChangePasswordModel) {

        btnDone.setOnClickListener {

            getValidPostData(changePasswordModel)?.also {
                (viewModel as PasswordViewModel).postChangePasswordModel(it, model)
            }
        }
    }

    private fun setLiveDataObserver() {

        (viewModel as PasswordViewModel).apply {

            changePasswordLD.observe(viewLifecycleOwner, Observer { changePasswordModel ->

                setupView(changePasswordModel)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            actionSuccessLD.observe(viewLifecycleOwner, Observer { actionSuccess ->

                if (actionSuccess) {
                    etOldPassword?.text?.clear()
                    etNewPassword?.text?.clear()
                    etConfirmPassword?.text?.clear()
                }
            })
        }
    }

    private fun getValidPostData(changePasswordModel: ChangePasswordModel): ChangePasswordModel? {

        val etUtils = EditTextUtils()

        val old = etUtils.showToastIfEmpty(etOldPassword) ?: return null
        val new = etUtils.showToastIfEmpty(etNewPassword) ?: return null
        val confirmNew = etUtils.showToastIfEmpty(etConfirmPassword) ?: return null

        if(new != confirmNew) {
            toast(R.string.password_mismatch)
            return null
        }

        return changePasswordModel.apply {
            data?.oldPassword = old
            data?.newPassword = new
            data?.confirmNewPassword = confirmNew
        }
    }
}