package com.bs.ecommerce.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.auth.forgotpass.ChangePasswordFragment
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import java.util.*

open class RegisterFragment : ToolbarLogoBaseFragment(), View.OnClickListener
{
    override fun getFragmentTitle() = R.string.title_register

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout? = register_root_layout

    override fun createViewModel(): BaseViewModel = RegistrationViewModel()

    protected var customAttributeManager: CustomAttributeManager? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    var isValidInfo = true

    private var myCalendar: Calendar? = null

    private var showPasswordField = false

    var customerInfo: GetRegistrationResponse  = GetRegistrationResponse()

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

            hideTextPanels()
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
                    if(this@RegisterFragment is CustomerInfoFragment) {
                        val oldData = prefObject.getCustomerInfo(PrefSingleton.CUSTOMER_INFO)

                        oldData?.firstName = getRegistrationResponse?.data?.firstName
                        oldData?.lastName = getRegistrationResponse?.data?.lastName
                        oldData?.email = getRegistrationResponse?.data?.email

                        prefObject.setCustomerInfo(PrefSingleton.CUSTOMER_INFO, oldData)

                        toast(getRegistrationResponse.message ?: getString(R.string.customer_info_updated))
                    }
                }

            })

            isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            actionSuccessLD.observe(viewLifecycleOwner, Observer { actionSuccess ->
                if(actionSuccess) {
                    if(this@RegisterFragment !is CustomerInfoFragment) {
                        requireActivity().onBackPressed()
                    }
                }
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

            enterPasswordEditText?.showOrHideOrRequired(isEnabledParam = showPasswordField, isRequired =   showPasswordField)

            confirmPasswordEditText?.showOrHideOrRequired(isEnabledParam = showPasswordField, isRequired =   showPasswordField)

            dateOfBirthTextView?.showOrHideOrRequired(isEnabledParam = dateOfBirthEnabled, isRequired =   dateOfBirthRequired,
                value = TextUtils().getFormattedDate(dateOfBirthDay, dateOfBirthMonth, dateOfBirthYear))

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

            passwordLayout?.showOrHide(this@RegisterFragment !is CustomerInfoFragment)


            tvChangePassword?.visibility = if(this@RegisterFragment is CustomerInfoFragment)
                View.VISIBLE else View.GONE

            tvChangePassword.setOnClickListener {
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


            val dialog = DatePickerDialog(activity!!, dateSetListener,
                calender!!.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.maxDate = System.currentTimeMillis()
            dialog.show()
        }

        saveBtn?.setOnClickListener {

            requireActivity().hideKeyboard()

            getCustomerInfoWithValidation()

            performSubmit()
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
            toast("${editText.hint} ${getString(R.string.reg_hint_is_required)}" )
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

            
            newsletter = cbNewsletter?.isChecked!!

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


}
