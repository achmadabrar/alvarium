package com.bs.ecommerce.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import java.util.*

open class RegisterFragment : BaseFragment(), View.OnClickListener
{
    override fun getFragmentTitle() = R.string.title_register

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout? = register_root_layout

    override fun createViewModel(): BaseViewModel = RegistrationViewModel()

    private var customerAttributeView: CustomerAttributeView? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    var isValidInfo = true

    internal var myCalendar: Calendar? = null

    private var viewCreated = false


    var customerInfo: GetRegistrationResponse  = GetRegistrationResponse()

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

    lateinit var model: AuthModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        if (!viewCreated) {

            initView()

            hideTextPanels()
            initEditButtonsAction()

            model = AuthModelImpl()

            viewModel  = ViewModelProvider(this).get(RegistrationViewModel::class.java)

            activity?.supportFragmentManager?.findFragmentById(R.id.layoutFrame)?.let {

                if(it !is CustomerInfoFragment)
                    (viewModel as RegistrationViewModel).getRegistrationVM(model)
            }

            viewCreated = true
        }


        setLiveDataListeners()

    }

    private fun setLiveDataListeners()
    {
        (viewModel as RegistrationViewModel).apply {

            getRegistrationResponseLD.observe(requireActivity(), Observer { getRegistrationResponse ->

                if(getRegistrationResponse.errorList.isNotEmpty())
                    toast(TextUtils().getHtmlFormattedText(getRegistrationResponse?.errorsAsFormattedString.toString()).toString())
                else
                    setViewsInitially(getRegistrationResponse.data)

            })

            isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }


    }

    private fun setViewsInitially(data: GetRegisterData)
    {
        with(data)
        {

            customerFirstNameEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = firstName)

            customerLastNameEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = lastName)

            emailEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true, value = email)

            enterPasswordEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true)

            confirmPasswordEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true)

            dateOfBirthTextView?.showOrHideOrRequired(isEnabledParam = dateOfBirthEnabled, isRequired =   dateOfBirthRequired, value = "")

            usernameEditText?.showOrHideOrRequired(isEnabledParam = usernamesEnabled, isRequired =   false, value = username)

            companyInfoEditText?.showOrHideOrRequired(isEnabledParam = companyEnabled, isRequired =   companyRequired, value = company)

            streetAddressEditText?.showOrHideOrRequired(isEnabledParam = streetAddressEnabled, isRequired =   streetAddressRequired, value = streetAddress)

            streetAddress2EditText?.showOrHideOrRequired(isEnabledParam = streetAddress2Enabled, isRequired =   streetAddress2Required, value = streetAddress2)

            zipOrPostalCodeEditText?.showOrHideOrRequired(isEnabledParam = zipPostalCodeEnabled, isRequired =   zipPostalCodeRequired, value = zipPostalCode)

            cityEditText?.showOrHideOrRequired(isEnabledParam = cityEnabled, isRequired =   cityRequired, value = city)

            countryEditText?.showOrHideOrRequired(isEnabledParam = countryEnabled, isRequired =   countryRequired, value = county)

            stateProvinceEditText?.showOrHideOrRequired(isEnabledParam = stateProvinceEnabled, isRequired =   stateProvinceRequired)

            phoneEditText?.showOrHideOrRequired(isEnabledParam = phoneEnabled, isRequired =   phoneRequired, value = phone)

            faxEditText?.showOrHideOrRequired(isEnabledParam = faxEnabled, isRequired =   faxRequired, value = fax)



            genderLayout?.showOrHide(genderEnabled)

            newsletterLayout?.showOrHide(newsletterEnabled)

            privacyPolicyLayout?.showOrHide(acceptPrivacyPolicyEnabled)



            bottomSheetLayoutRegister?.let {

                customerAttributeView = CustomerAttributeView(requireContext(), viewModel as RegistrationViewModel, it, bsBehavior)

                for (i in customerAttributeView!!.getAttrViews()) {
                    attrViewHolderRegisterPage?.addView(i)
                }

            }

        }





        rootScrollView?.visibility = View.VISIBLE

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

            getCustomerInfoWithValidation()

            performSubmit()
        }


    }

    open fun performSubmit()
    {
        if(isValidInfo)
            (viewModel as RegistrationViewModel).postRegisterVM(customerInfo, model)
    }

    private fun showValidation( editText: EditText, isRequired: Boolean)
    {
        if(isRequired)
        {
            isValidInfo = false
            toast("${editText.hint} ${getString(R.string.reg_hint_is_required)}" )
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
    }

    fun initView() {

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutRegister)
        bsBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetLayoutRegister?.tvDone?.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.tvDone -> {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                customerAttributeView?.onBottomSheetClose()
            }
        }
    }


}
