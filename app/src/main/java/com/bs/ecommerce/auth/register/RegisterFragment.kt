package com.bs.ecommerce.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.auth.register.data.CustomerRegistrationInfo
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.customViews.CustomerAttributeViews
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*

class RegisterFragment : BaseFragment()
{
    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout? = register_root_layout

    override fun createViewModel(): BaseViewModel = RegistrationViewModel()


    private var isValidInfo = false

    internal var myCalendar: Calendar? = null

    private var customerInfo: CustomerRegistrationInfo? = null

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

        (viewModel as RegistrationViewModel).getRegistrationData(model)


        setLiveDataListeners()

    }

    private fun setLiveDataListeners()
    {

        (viewModel as RegistrationViewModel).getRegistrationResponseLD.observe(requireActivity(), Observer { getRegistrationResponse ->



            setViewsInitially(getRegistrationResponse.data)

            /* if (getRegistrationResponse?.statusCode == 400)
            {
                saveBtn.isEnabled = true

                var errors = getString(R.string.error_register_customer) + "\n"

                if (response.errorList.isNotEmpty())
                {
                    for (i in 0 until response.errorList.size)
                    {
                        errors += "  " + (i + 1) + ": " + response.errorList[i] + " \n"
                    }
                    Toast.makeText(activity, errors, Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(activity, response?.successMessage!!, Toast.LENGTH_LONG).show()

                callLoginWebservice(LoginData(customerInfo?.email.toString(), customerInfo?.password.toString()))
            }*/


            //TODO set customer attributes
/*            dynamicAttributeLayoutParent.visibility = View.VISIBLE
            customerAttributeViews = CustomerAttributeViews(activity!!, response.data, dynamicAttributeLayout_customer_info!!, this)*/
        })

        (viewModel as RegistrationViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }

    private fun showOrHide(isEnabledParam: Boolean, editText: EditText?)
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

            showOrHide(dateOfBirthEnabled, dateOfBirthTextView)

            showOrHide(genderEnabled, genderLayout)

            showOrHide(usernamesEnabled, usernameEditText)
            showOrHide(companyEnabled, companyInfoEditText)
            showOrHide(streetAddressEnabled, streetAddressEditText)
            showOrHide(streetAddress2Enabled, streetAddress2EditText)
            showOrHide(zipPostalCodeEnabled, zipOrPostalCodeEditText)
            showOrHide(cityEnabled, cityEditText)
            showOrHide(countryEnabled, countryEditText)
            showOrHide(stateProvinceEnabled, stateProvinceEditText)
            showOrHide(phoneEnabled, phoneEditText)
            showOrHide(faxEnabled, faxEditText)
            showOrHide(newsletterEnabled, newsletterLayout)
            showOrHide(acceptPrivacyPolicyEnabled, privacyPolicyLayout)

        }

    }

    private fun hideTextPanels()
    {
        saveBtn?.text = getString(R.string.register_new)
        saveBtn.isEnabled = false
        saveBtn?.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.browser_actions_bg_grey))
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
            performRegistration()
            //saveBtn?.isEnabled = false
            activity?.hideKeyboard()
        }


    }

    private fun performRegistration()
    {
        performValidation()

        /*if (isValidInfo)
            callRegisterWebService(getCustomerInfo())
        else
            toast(R.string.fill_required_fields)*/

    }

    private fun performValidation()
    {
        getCustomerInfo()

        customerInfo?.let {

            if (it.firstName!!.isNotEmpty() &&
                it.lastName!!.isNotEmpty() &&
                it.email!!.isNotEmpty() &&
                it.password!!.isNotEmpty() &&
                it.confirmPassword!!.isNotEmpty())
                isValidInfo = true

        }
    }

    private fun getCustomerInfo() : CustomerRegistrationInfo
    {
        customerInfo = CustomerRegistrationInfo()
        customerInfo?.firstName = customerFirstNameEditText?.text?.trim().toString()
        customerInfo?.lastName = customerLastNameEditText?.text?.trim().toString()
        customerInfo?.email = emailEditText?.text?.trim().toString()
        customerInfo?.phone = phoneEditText?.text?.trim().toString()
        customerInfo?.company = companyInfoEditText?.text.toString()

        if (myCalendar != null)
        {
            customerInfo?.dateOfBirthYear = myCalendar!!.get(Calendar.YEAR)
            customerInfo?.dateOfBirthMonth = myCalendar!!.get(Calendar.MONTH) + 1
            customerInfo?.dateOfBirthDay = myCalendar!!.get(Calendar.DAY_OF_MONTH)
        }
        if (genderMaleRadioButton.isChecked)
            customerInfo?.gender = "M"
        else if (genderFemaleRadioButton.isChecked)
            customerInfo?.gender = "F"

        customerInfo?.isNewsletter = cbNewsletter?.isChecked!!
        customerInfo?.password = enterPasswordEditText?.text?.toString()
        customerInfo?.confirmPassword = confirmPasswordEditText?.text?.toString()

        customerInfo?.username = usernameEditText?.text?.toString()


        //customerAttributeViews?.putEdittextValueInMap()

        //customerInfo?.formValue = customerAttributeViews?.productAttribute
        //"attributestTest".showLog(customerAttributeViews?.productAttribute.toString())


        return customerInfo as CustomerRegistrationInfo
    }


}
