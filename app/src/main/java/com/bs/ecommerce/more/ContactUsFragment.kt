package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.viewmodel.ContactUsViewMode
import com.bs.ecommerce.utils.isEmailValid
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_contact_us.*

class ContactUsFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_contact_us

    override fun getRootLayout(): RelativeLayout? = contactUsRootLayout

    override fun createViewModel(): BaseViewModel = ContactUsViewMode()

    override fun getFragmentTitle(): Int = R.string.title_contact_us

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            setupView()
        }
    }

    private fun setupView() {
        btnSubmit.setOnClickListener { submitIfFormIsValid() }
    }

    private fun submitIfFormIsValid() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val enquiry = etEnquiry.text.toString().trim()

        if(name.isEmpty()) {
            toast(getString(R.string.validation_toast, etName.hint.toString()))
            return
        }

        if(email.isEmpty() || !email.isEmailValid()) {
            toast(getString(R.string.validation_toast, etEmail.hint.toString()))
            return
        }

        if(enquiry.isEmpty()) {
            toast(getString(R.string.validation_toast, etEnquiry.hint.toString()))
            return
        }

        // TODO api call here
    }


}