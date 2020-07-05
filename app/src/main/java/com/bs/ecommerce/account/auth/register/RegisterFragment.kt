package com.bs.ecommerce.account.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.AuthModel
import com.bs.ecommerce.account.auth.AuthModelImpl
import com.bs.ecommerce.account.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.account.auth.forgotpass.ChangePasswordFragment
import com.bs.ecommerce.account.auth.register.data.GetRegisterData
import com.bs.ecommerce.account.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.checkout.CheckoutViewModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*

open class RegisterFragment : ToolbarLogoBaseFragment(), View.OnClickListener
{
    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_REGISTER)

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout? = register_root_layout

    override fun createViewModel(): BaseViewModel =
        RegistrationViewModel()

    protected var customAttributeManager: CustomAttributeManager? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    var isValidInfo = true

    private var myCalendar: Calendar? = null

    private var showPasswordField = false

    var customerInfo: GetRegistrationResponse =
        GetRegistrationResponse()

    private val dateSetListener: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener {

                view, year, monthOfYear, dayOfMonth ->

            myCalendar = Calendar.getInstance()
            myCalendar?.set(Calendar.YEAR, year)
            myCalendar?.set(Calendar.MONTH, monthOfYear)
            myCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateOfBirthTextView?.setText(
                TextUtils().getFormattedDate(
                    dayOfMonth,
                    monthOfYear + 1,
                    year
                )
            )

            saveBtn?.visibility = View.VISIBLE
        }
    }

    lateinit var model: AuthModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        if (!viewCreated) {

            initView()

            saveBtn?.text = DbHelper.getString(Const.REGISTER_BUTTON)
            initEditButtonsAction()

            model = AuthModelImpl()

            viewModel  = ViewModelProvider(this).get(RegistrationViewModel::class.java)

            requireActivity().supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

                if(it !is CustomerInfoFragment) {
                    (viewModel as RegistrationViewModel).getRegistrationVM(model)
                    showPasswordField = true
                }
            }
        }

        setLiveDataListeners()

    }

    private fun setLiveDataListeners()
    {
        (viewModel as RegistrationViewModel).apply {

            getRegistrationResponseLD.observe(requireActivity(), Observer { getRegistrationResponse ->

                if(getRegistrationResponse.errorList.isNotEmpty()) {
                    toast(getRegistrationResponse?.errorsAsFormattedString.toString())
                } else {
                    setViewsInitially(getRegistrationResponse.data)
                    customerInfo = getRegistrationResponse

                    focusStealer?.requestFocus()

                    // Update shared pref data for
                    if(this@RegisterFragment is CustomerInfoFragment && customerInfoUpdate) {
                        val oldData = prefObject.getCustomerInfo(PrefSingleton.CUSTOMER_INFO)

                        oldData?.firstName = getRegistrationResponse?.data?.firstName
                        oldData?.lastName = getRegistrationResponse?.data?.lastName
                        oldData?.email = getRegistrationResponse?.data?.email

                        prefObject.setCustomerInfo(PrefSingleton.CUSTOMER_INFO, oldData)

                        toast(getRegistrationResponse.message ?: DbHelper.getString(Const.UPDATED_SUCCESSFULLY))

                        customerInfoUpdate = false
                    }
                }

            })

            isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            registrationSuccessLD.observe(viewLifecycleOwner, Observer { actionSuccess ->
                if(actionSuccess) {
                    if(this@RegisterFragment !is CustomerInfoFragment) {
                        requireActivity().onBackPressed()
                    }
                }
            })

            stateListLD.observe(viewLifecycleOwner, Observer { statesList ->
                populateStateSpinner(getRegistrationResponseLD.value?.data?.stateProvinceId ?: 0, statesList)
            })
        }


    }

    private fun setViewsInitially(data: GetRegisterData)
    {
        cbNewsletter?.text = DbHelper.getString(Const.NEWSLETTER)
        tvChangePassword?.text = DbHelper.getString(Const.CHANGE_PASSWORD)
        tvOptions?.text = DbHelper.getString(Const.OPTIONS)
        tvPassword?.text = DbHelper.getString(Const.REGISTRATION_PASSWORD)
        tvGender?.text = DbHelper.getString(Const.GENDER)
        tvPersonalDetails?.text = DbHelper.getString(Const.REGISTRATION_PERSONAL_DETAILS)
        genderMaleRadioButton?.text = DbHelper.getString(Const.GENDER_MALE)
        genderFemaleRadioButton?.text = DbHelper.getString(Const.GENDER_FEMALE)

        with(data)
        {

            customerFirstNameEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = firstName,
                hintText = DbHelper.getString(Const.FIRST_NAME))

            customerLastNameEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = lastName,
                hintText = DbHelper.getString(Const.LAST_NAME))

            emailEditText?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true, value = email,
                hintText = DbHelper.getString(Const.EMAIL))

            enterPasswordEditText?.showOrHideOrRequired(isEnabledParam = showPasswordField, isRequired =   showPasswordField,
                hintText = DbHelper.getString(Const.ENTER_PASSWORD))

            confirmPasswordEditText?.showOrHideOrRequired(isEnabledParam = showPasswordField, isRequired =   showPasswordField,
                hintText = DbHelper.getString(Const.CONFIRM_PASSWORD))

            dateOfBirthTextView?.showOrHideOrRequired(isEnabledParam = dateOfBirthEnabled, isRequired =   dateOfBirthRequired,
                value = TextUtils().getFormattedDate(dateOfBirthDay, dateOfBirthMonth, dateOfBirthYear), hintText = DbHelper.getString(Const.DATE_OF_BIRTH))

            usernameEditText?.showOrHideOrRequired(isEnabledParam = usernamesEnabled, isRequired =   false, value = username,
                hintText = DbHelper.getString(Const.USERNAME))

            companyInfoEditText?.showOrHideOrRequired(isEnabledParam = companyEnabled, isRequired =   companyRequired, value = company,
                 hintText = DbHelper.getString(Const.COMPANY))

            streetAddressEditText?.showOrHideOrRequired(isEnabledParam = streetAddressEnabled, isRequired =   streetAddressRequired, value = streetAddress,
                hintText = DbHelper.getString(Const.STREET_ADDRESS))

            streetAddress2EditText?.showOrHideOrRequired(isEnabledParam = streetAddress2Enabled, isRequired =   streetAddress2Required, value = streetAddress2,
                hintText = DbHelper.getString(Const.STREET_ADDRESS_2))

            zipOrPostalCodeEditText?.showOrHideOrRequired(isEnabledParam = zipPostalCodeEnabled, isRequired =   zipPostalCodeRequired, value = zipPostalCode,
                hintText = DbHelper.getString(Const.ZIP_CODE))

            cityEditText?.showOrHideOrRequired(isEnabledParam = cityEnabled, isRequired =   cityRequired, value = city,
                hintText = DbHelper.getString(Const.CITY))

            phoneEditText?.showOrHideOrRequired(isEnabledParam = phoneEnabled, isRequired =   phoneRequired, value = phone,
                hintText = DbHelper.getString(Const.PHONE))

            faxEditText?.showOrHideOrRequired(isEnabledParam = faxEnabled, isRequired =   faxRequired, value = fax,
                hintText = DbHelper.getString(Const.FAX))

            if(countryEnabled) {
                populateCountrySpinner(availableCountries)
            }

            genderLayout?.showOrHide(genderEnabled)

            newsletterLayout?.showOrHide(newsletterEnabled)

            privacyPolicyLayout?.showOrHide(acceptPrivacyPolicyEnabled)

            passwordLayout?.showOrHide(this@RegisterFragment !is CustomerInfoFragment)


            tvChangePassword?.visibility = if(this@RegisterFragment is CustomerInfoFragment)
                View.VISIBLE else View.GONE

            tvChangePassword?.setOnClickListener {
                replaceFragmentSafely(ChangePasswordFragment())
            }

            // setup product attributes
            customAttributeManager =
                CustomAttributeManager(
                    attributes = data.customerAttributes ?: listOf(),
                    attributeViewHolder = attrViewHolderRegisterPage,
                    attributeValueHolder = bottomSheetLayoutRegister.attributeValueHolder,
                    bsBehavior = bsBehavior
                )

            customAttributeManager?.attachAttributesToFragment()

        }


        rootScrollView?.visibility = View.VISIBLE

    }

    private fun initEditButtonsAction()
    {

        dateOfBirthTextView?.setOnClickListener {
            var calender: Calendar? = Calendar.getInstance()

            if (myCalendar != null)
                calender = myCalendar


            val dialog = DatePickerDialog(requireContext(), dateSetListener,
                calender!!.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.maxDate = System.currentTimeMillis()
            dialog.show()
        }

        saveBtn?.setOnClickListener {

            requireActivity().hideKeyboard()

            try {
                getCustomerInfoWithValidation()
                performSubmit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    open fun performSubmit()
    {
        if(isValidInfo) {
            // custom attribute fields value
            val formValue = customAttributeManager
                ?.getFormData(Api.customerAttributePrefix) ?: KeyValueFormData()

            customerInfo.formValues = formValue.formValues

            (viewModel as RegistrationViewModel).postRegisterVM(customerInfo, model)
        }
    }

    private fun showValidation( editText: EditText, isRequired: Boolean)
    {
        if(isRequired)
        {
            isValidInfo = false
            toast("${editText.hint} ${DbHelper.getString(Const.IS_REQUIRED)}" )
        }

    }

    
    private fun getCustomerInfoWithValidation()
    {
        isValidInfo = true

        with(customerInfo.data)
        {

            confirmPasswordEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) confirmPassword = input else { showValidation(it, showPasswordField) }
            }

            enterPasswordEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) password = input else { showValidation(it, showPasswordField) }
            }

            dateOfBirthTextView?.let {
                val input = it.text?.trim().toString(); if(input.isEmpty()) { showValidation(it, dateOfBirthRequired) }
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

            var selectedCountryId = 0

            if(countrySpinner.selectedItem is AvailableCountry)
                selectedCountryId = (countrySpinner.selectedItem as AvailableCountry).value.toInt()

            if (countryEnabled && selectedCountryId == 0) {
                isValidInfo = false
                toast(DbHelper.getString(Const.COUNTRY_REQUIRED))
            } else {
                countryId = selectedCountryId
            }

            if(stateSpinner.selectedItem is AvailableState) {
                stateProvinceId = (stateSpinner.selectedItem as AvailableState).id
            }


            phoneEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) phone = input else { showValidation(it, phoneRequired)}
            }

            emailEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()  && input.isEmailValid()) email = input else {showValidation(it, true)}
            }


            myCalendar?.get(Calendar.YEAR)?.also { dateOfBirthYear = it }
            myCalendar?.get(Calendar.MONTH)?.also { dateOfBirthMonth = it + 1 }
            myCalendar?.get(Calendar.DAY_OF_MONTH)?.also { dateOfBirthDay = it }

            gender = when {
                genderMaleRadioButton.isChecked -> "M"
                genderFemaleRadioButton.isChecked -> "F"
                else -> ""
            }

            customerLastNameEditText?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) lastName = input else { showValidation(it, true) }
            }

            customerFirstNameEditText?.let {

                val input = it.text?.trim().toString(); if(input.isNotEmpty()) firstName = input else { showValidation(it, true) }
            }

            
            newsletter = cbNewsletter?.isChecked ?: false

        }
    }

    fun initView() {
        focusStealer?.requestFocus()

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutRegister)

        bottomSheetLayoutRegister?.tvDone?.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.tvDone -> {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                //customerAttributeView?.onBottomSheetClose()
            }
        }
    }

    private fun populateCountrySpinner(availableCountries: List<AvailableCountry>) {

        val countryAdapter: ArrayAdapter<AvailableCountry> =
            ArrayAdapter<AvailableCountry>(requireContext(), R.layout.simple_spinner_item, availableCountries)

        countryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        countrySpinner?.adapter = countryAdapter

        countrySpinnerLayout?.visibility = View.VISIBLE

        countrySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // load states for selected country
                if (availableCountries[position].value != "0") {
                    (viewModel as CheckoutViewModel).getStatesByCountryVM(
                        availableCountries[position].value, CheckoutModelImpl()
                    )
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // set selected country
        availableCountries.indexOfFirst { it.selected }.also {
            if (it >= 0) countrySpinner.setSelection(it)
        }
    }

    private fun populateStateSpinner(selectedStateId: Int?, states: List<AvailableState>) {

        val spinnerAdapter: ArrayAdapter<AvailableState> =
            ArrayAdapter<AvailableState>(requireContext(), R.layout.simple_spinner_item, states)

        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        stateSpinner?.adapter = spinnerAdapter

        stateSpinnerLayout?.visibility = View.VISIBLE

        // set selected state
        states.indexOfFirst { it.id == selectedStateId }.also {
            if (it >= 0) stateSpinner.setSelection(it)
        }
    }


}
