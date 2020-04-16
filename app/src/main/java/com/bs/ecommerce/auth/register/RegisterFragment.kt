package com.bs.ecommerce.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.utils.hideKeyboard
import com.bs.ecommerce.utils.toast

import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*

class RegisterFragment : BaseFragment()
{
    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout? = register_root_layout

    override fun createViewModel(): BaseViewModel = RegistrationViewModel()


    private var isValidInfo = true

    internal var myCalendar: Calendar? = null


    private var customerInfo: GetRegistrationResponse  = GetRegistrationResponse()

    internal var dateSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {

            view, year, monthOfYear, dayOfMonth ->

        myCalendar = Calendar.getInstance()
        myCalendar?.set(Calendar.YEAR, year)
        myCalendar?.set(Calendar.MONTH, monthOfYear)
        myCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val month = monthOfYear + 1
        dateOfBirthTextView?.setText("$dayOfMonth / $month / $year")

        saveBtn?.visibility = View.VISIBLE
    }

    private lateinit var model: AuthModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.register)

        hideTextPanels()
        initEditButtonsAction()

        model = AuthModelImpl()

        viewModel  = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        (viewModel as RegistrationViewModel).getRegistrationVM(model)


        setLiveDataListeners()

    }

    private fun setLiveDataListeners()
    {

        (viewModel as RegistrationViewModel).getRegistrationResponseLD.observe(requireActivity(), Observer { getRegistrationResponse ->


            if(getRegistrationResponse.errorList.isNotEmpty())
                toast(getRegistrationResponse?.errorsAsFormattedString.toString())
            else
                setViewsInitially(getRegistrationResponse.data)

        })

        (viewModel as RegistrationViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }

    private fun showOrHide(isEnabledParam: Boolean = false, editText: EditText?)
    {
        if (isEnabledParam)
            editText?.visibility = View.VISIBLE
        else
            editText?.visibility = View.GONE
    }

    private fun showOrHide(isEnabledParam: Boolean, layout: LinearLayout?)
    {
        if (isEnabledParam)
            layout?.visibility = View.VISIBLE
        else
            layout?.visibility = View.GONE
    }

    private fun setViewsInitially(data: GetRegisterData)
    {

        with(data)
        {

            showOrHide(dateOfBirthEnabled, dateOfBirthTextView as EditText)

            showOrHide(genderEnabled,  genderLayout)

            showOrHide(usernamesEnabled,  usernameEditText)

            showOrHide(companyEnabled,  companyInfoEditText)

            showOrHide(streetAddressEnabled, streetAddressEditText)

            showOrHide(streetAddress2Enabled, streetAddress2EditText)

            showOrHide(zipPostalCodeEnabled, zipOrPostalCodeEditText)

            showOrHide(cityEnabled,  cityEditText)

            showOrHide(countryEnabled,  countryEditText)

            showOrHide(stateProvinceEnabled,  stateProvinceEditText)

            showOrHide(phoneEnabled,  phoneEditText)

            showOrHide(faxEnabled,  faxEditText)

            showOrHide(newsletterEnabled, newsletterLayout)

            showOrHide(acceptPrivacyPolicyEnabled, privacyPolicyLayout)

        }

    }

    private fun hideTextPanels()
    {
        saveBtn?.text = getString(R.string.register_new)
/*        saveBtn.isEnabled = false
        saveBtn?.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.browser_actions_bg_grey))*/
    }


    private fun initEditButtonsAction()
    {

        dateOfBirthTextView?.setOnClickListener {
            var calender: Calendar? = Calendar.getInstance()

            if (myCalendar != null)
                calender = myCalendar


            DatePickerDialog(activity!!, dateSetListener,
                calender!!.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)).show()
        }

        saveBtn?.setOnClickListener {

            requireActivity().hideKeyboard()
            performRegistration()
        }


    }

    private fun performRegistration()
    {
        getCustomerInfoWithValidation()

        if(isValidInfo)
            (viewModel as RegistrationViewModel).postRegisterVM(customerInfo, model)
    }

    fun CharSequence?.isEmailValid() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

    private fun showValidation( editText: EditText, isRequired: Boolean)
    {
        if(isRequired)
        {
            isValidInfo = false
            toast("${editText.hint} is required" )
        }
        else
            isValidInfo = true

    }

    
    private fun getCustomerInfoWithValidation()
    {

        with(customerInfo.data)
        {

            confirmPasswordEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) confirmPassword = input else { showValidation(it, true) }
            }

            enterPasswordEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) password = input else { showValidation(it, true) }
            }

            companyInfoEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) company = input else { showValidation(it, companyRequired)}
            }

            usernameEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) username = input else { showValidation(it, false)}
            }

            streetAddressEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) streetAddress = input else { showValidation(it, streetAddressRequired) }
            }

            streetAddress2EditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) streetAddress2 = input else { showValidation(it, streetAddress2Required) }
            }

            zipOrPostalCodeEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) zipPostalCode = input else { showValidation(it, zipPostalCodeRequired)}
            }

            cityEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) city = input else { showValidation(it, cityRequired) }
            }

            countryEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) county = input else { showValidation(it, countryRequired)}
            }

            stateProvinceEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) stateProvinceId = input.toInt() else { showValidation(it, stateProvinceRequired)}
            }


            phoneEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) phone = input else { showValidation(it, phoneRequired)}
            }

            emailEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()  && input.isEmailValid()) email = input else {showValidation(it, true)}
            }


            if (myCalendar != null)
            {
                dateOfBirthYear = myCalendar!!.get(Calendar.YEAR).toString()
                dateOfBirthMonth = myCalendar!!.get(Calendar.MONTH).toString() + 1
                dateOfBirthDay = myCalendar!!.get(Calendar.DAY_OF_MONTH).toString()
            }
            if (genderMaleRadioButton.isChecked)
                gender = "M"
            else if (genderFemaleRadioButton.isChecked)
                gender = "F"

            customerLastNameEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) lastName = input else { showValidation(it, true) }
            }

            customerFirstNameEditText?.let {

                val input = it.text?.trim().toString(); if(input.isNotEmpty()) firstName = input else { showValidation(it, true) }
            }

            
            newsletter = cbNewsletter?.isChecked!!

        }

        //customerAttributeViews?.putEdittextValueInMap()

        //customerInfo?.formValue = customerAttributeViews?.productAttribute
        //"attributestTest".showLog(customerAttributeViews?.productAttribute.toString())
    }


}
