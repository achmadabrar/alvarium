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
import com.bs.ecommerce.more.viewmodel.VendorViewModel
import com.bs.ecommerce.product.model.VendorModel
import com.bs.ecommerce.product.model.VendorModelImpl
import com.bs.ecommerce.product.model.data.ContactVendorModel
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.EditTextUtils
import com.bs.ecommerce.utils.isEmailValid
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_contact_us.*

class ContactVendorFragment: BaseFragment() {

    private lateinit var model: VendorModel

    override fun getLayoutId(): Int = R.layout.fragment_contact_us

    override fun getRootLayout(): RelativeLayout? = contactUsRootLayout

    override fun createViewModel(): BaseViewModel = VendorViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.VENDOR_CONTACT_VENDOR)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = VendorModelImpl()
            viewModel = ViewModelProvider(this).get(VendorViewModel::class.java)

            arguments?.getInt(VENDOR_ID)?.let {
                (viewModel as VendorViewModel).getContactVendorModel(it, model)
            }
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {

        (viewModel as VendorViewModel).apply {

            contactVendorModelLD.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    setupView(it)
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            enquirySendSuccessLD.observe(viewLifecycleOwner, Observer { success ->

                if (success.getContentIfNotHandled() == true) {
                    etName?.text?.clear()
                    etEmail?.text?.clear()
                    etEnquiry?.text?.clear()
                    etSubject?.text?.clear()
                }
            })
        }
    }

    private fun setupView(data: ContactVendorModel) {

        if(data.subjectEnabled == true) {
            etSubject.visibility = View.VISIBLE
            etSubject.hint = DbHelper.getString(Const.VENDOR_SUBJECT)
        }

        etName.hint = DbHelper.getString(Const.VENDOR_FULLNAME)
        etEmail.hint = DbHelper.getString(Const.VENDOR_EMAIL)
        etEnquiry.hint = DbHelper.getString(Const.VENDOR_ENQUIRY)
        btnSubmit.text = DbHelper.getString(Const.VENDOR_BUTTON)

        btnSubmit.setOnClickListener { submitIfFormIsValid(data) }

        etName.visibility = View.VISIBLE
        etEmail.visibility = View.VISIBLE
        etEnquiry.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
    }

    private fun submitIfFormIsValid(data: ContactVendorModel) {
        val etUtil = EditTextUtils()

        val name = etUtil.showToastIfEmpty(etName,
            DbHelper.getString(Const.VENDOR_REQUIRED_FULLNAME)) ?: return

        val email = etUtil.showToastIfEmpty(etEmail,
            DbHelper.getString(Const.VENDOR_REQUIRED_EMAIL)) ?: return

        val enquiry = etUtil.showToastIfEmpty(etEnquiry,
            DbHelper.getString(Const.VENDOR_REQUIRED_ENQUIRY)) ?: return

        if(!email.isEmailValid()) {
            toast(DbHelper.getString(Const.ENTER_VALID_EMAIL))
            return
        }

        data.fullName = name
        data.email = email
        data.enquiry = enquiry
        data.subject = if(data.subjectEnabled == true) etSubject.text.toString().trim()
            else null

        (viewModel as VendorViewModel).postVendorEnquiry(data, model)
    }

    companion object {
        @JvmStatic
        private val VENDOR_NAME = "categoryName"

        @JvmStatic
        private val VENDOR_ID = "categoryId"

        @JvmStatic
        fun newInstance(vendorId: Int, vendorName: String?): ContactVendorFragment {
            val fragment = ContactVendorFragment()

            fragment.arguments = Bundle().apply {
                putString(VENDOR_NAME, vendorName ?: "")
                putInt(VENDOR_ID, vendorId)
            }

            return fragment
        }
    }
}