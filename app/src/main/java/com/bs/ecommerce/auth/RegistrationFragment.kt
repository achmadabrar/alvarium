package com.bs.ecommerce.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.data.CustomerRegistrationInfo
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.customViews.CustomerAttributeViews
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.main.model.AuthModelImpl
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.category_left.*
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*

class RegistrationFragment : BaseFragment()
{

    private lateinit var model: AuthModel

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getRootLayout(): RelativeLayout = register_root_layout

    override fun createViewModel(): RegistrationViewModel = RegistrationViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        model = AuthModelImpl(activity?.applicationContext!!)

        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        (viewModel as RegistrationViewModel).getRegistrationData(model)


        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {

        (viewModel as RegistrationViewModel).getRegistrationResponseLD.observe(activity!!, Observer { response -> toast(response.data.toString()) })

        (viewModel as RegistrationViewModel).isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                progressBarRegister?.visibility = View.VISIBLE
            else
                progressBarRegister?.visibility = View.GONE
        })

    }





   /* private var isValidInfo = false

    internal var myCalendar: Calendar? = null

    protected var customerAttributeViews: CustomerAttributeViews? = null
    private var customerInfo: CustomerRegistrationInfo? = null

    internal var dateSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {

            view, year, monthOfYear, dayOfMonth ->

        myCalendar = Calendar.getInstance()
        myCalendar?.set(Calendar.YEAR, year)
        myCalendar?.set(Calendar.MONTH, monthOfYear)
        myCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val month = monthOfYear + 1
        dateOfBirthTextView?.text = "$dayOfMonth / $month / $year"

        saveBtn?.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

            = container?.inflate(R.layout.fragment_customer_basic_info)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        checkEventBusRegistration()
        activity?.title = getString(R.string.register)

        clearUI()
        hideTextPanels()
        initEditButtonsAction()

        callCustomerAttributeAPI()

    }

    private fun callCustomerAttributeAPI() =  RetroClient.api.getCustomerAttribute().enqueue(CustomCB(this.view!!))

    @Subscribe
    fun onEvent(response: CustomerAtrributeResponse?)
    {
        if (response?.data?.isNotEmpty()!!)
        {
            dynamicAttributeLayoutParent.visibility = View.VISIBLE
            customerAttributeViews = CustomerAttributeViews(activity!!, response.data, dynamicAttributeLayout_customer_info!!, this)
        }
    }


    private fun callRegisterWebService(customerInfo : CustomerRegistrationInfo)

            =  RetroClient.api.preformRegistration(customerInfo).enqueue(CustomCB(this.view!!))


    fun callLoginWebservice(loginData: LoginData)

            = RetroClient.api.performLogin(loginData).enqueue(CustomCB(this.view!!))



    @Subscribe
    fun onEvent(response: LoginResponse?)
    {
        if (response?.token != null)
        {
            prefObject.setPrefs(PrefSingleton.TOKEN_KEY, response.token)
            prefObject.setPrefs(PrefSingleton.LOGGED_PREFER_KEY, true)
            NetworkUtil.token = response.token

            replaceFragmentSafely(HomePageFragment(), R.id.container)
        }
    }

    @Subscribe
    fun onEvent(response: RegistrationResponse?)
    {
        if (response?.statusCode == 400)
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
        }
    }

    private fun hideTextPanels()
    {
        nameTitleTextView?.visibility = View.GONE
        customerNameTextView?.visibility = View.GONE
        emailTextView?.visibility = View.GONE
        companyInfoTextView?.visibility = View.GONE
        genderTextView?.visibility = View.GONE
        usernameTextView?.visibility = View.GONE
        saveBtn?.text = getString(R.string.register_new)
        dateOfBirthTextView?.text = "dd/mm/yyyy"
        passwordTextView?.visibility = View.GONE
        phoneTextView?.visibility = View.GONE


        maritalStatusRadioGroup.visibility = View.GONE
        maritalStatusTextViewlayout.visibility = View.GONE
        extraCurricularlayout.visibility = View.GONE
        favouriteQuestionlayout.visibility = View.GONE
    }

    private fun clearUI()
    {
        customerNameTextView?.text = ""
        dateOfBirthTextView?.text = ""
        emailTextView?.text = ""
        companyInfoTextView?.text = ""
        genderTextView?.text = ""
        usernameTextView?.text = ""
        phoneTextView?.text = ""

        customerFirstNameEditText?.setText("")
        customerLastNameEditText?.setText("")
        emailEditText?.setText("")
        companyInfoEditText?.setText("")
        usernameEditText?.setText("")
        phoneEditText?.setText("")

        et_favouriteQuestion.visibility = View.GONE
        et_extraCurricular.visibility = View.GONE

    }


    private fun initEditButtonsAction()
    {

        dateOfBirthTextView?.setOnClickListener {
            // TODO Auto-generated method stub
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

        if (isValidInfo)
            callRegisterWebService(getCustomerInfo())
        else
            toast(R.string.fill_required_fields)

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

        customerInfo?.isNewsletter = cbNewsLetter?.isChecked!!
        customerInfo?.password = enterPasswordEditText?.text?.toString()
        customerInfo?.confirmPassword = confirmPasswordEditText?.text?.toString()

        customerInfo?.username = usernameEditText?.text?.toString()


        customerAttributeViews?.putEdittextValueInMap()

        customerInfo?.formValue = customerAttributeViews?.productAttribute
        "attributestTest".showLog(customerAttributeViews?.productAttribute.toString())


        return customerInfo as CustomerRegistrationInfo
    }*/


}
