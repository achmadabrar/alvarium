package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.CommonModel
import com.bs.ecommerce.more.model.CommonModelImpl
import com.bs.ecommerce.more.viewmodel.ContactUsViewModel
import com.bs.ecommerce.product.model.data.ContactUsData
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.EditTextUtils
import com.bs.ecommerce.utils.isEmailValid
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_contact_us.*

class ContactUsFragment: BaseFragment() {

    private lateinit var model: CommonModel

    override fun getLayoutId(): Int = R.layout.fragment_contact_us

    override fun getRootLayout(): RelativeLayout? = contactUsRootLayout

    override fun createViewModel(): BaseViewModel = ContactUsViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.MORE_CONTACT_US)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = CommonModelImpl()
            viewModel = ViewModelProvider(this).get(ContactUsViewModel::class.java)

            setupView()
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {

        (viewModel as ContactUsViewModel).apply {

            contactUsLD.observe(viewLifecycleOwner, Observer { data ->

                if(data.successfullySent == true) {
                    etName?.text?.clear()
                    etEmail?.text?.clear()
                    etEnquiry?.text?.clear()
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    private fun setupView() {

        etName.hint = DbHelper.getString(Const.CONTACT_US_FULLNAME)
        etEmail.hint = DbHelper.getString(Const.CONTACT_US_EMAIL)
        etEnquiry.hint = DbHelper.getString(Const.CONTACT_US_ENQUIRY)

        btnSubmit.text = DbHelper.getString(Const.CONTACT_US_BUTTON)
        btnSubmit.setOnClickListener { submitIfFormIsValid() }

        etName.visibility = View.VISIBLE
        etEmail.visibility = View.VISIBLE
        etEnquiry.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
    }

    private fun submitIfFormIsValid() {
        val etUtil = EditTextUtils()

        val name = etUtil.showToastIfEmpty(etName,
            DbHelper.getString(Const.CONTACT_US_REQUIRED_FULLNAME)) ?: return

        val email = etUtil.showToastIfEmpty(etEmail,
            DbHelper.getString(Const.CONTACT_US_REQUIRED_EMAIL)) ?: return

        val enquiry = etUtil.showToastIfEmpty(etEnquiry,
            DbHelper.getString(Const.CONTACT_US_REQUIRED_ENQUIRY)) ?: return

        if(!email.isEmailValid()) {
            toast(DbHelper.getString(Const.ENTER_VALID_EMAIL))
            return
        }

        ContactUsData(
            name, email, enquiry
        ).let {
            (viewModel as ContactUsViewModel).postCustomersEnquiry(it, model)
        }
    }


}