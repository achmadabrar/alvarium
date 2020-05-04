package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.BillingAddressData
import com.bs.ecommerce.checkout.model.data.AddressModel
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.isEmailValid
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.showOrHideOrRequired
import com.bs.ecommerce.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.address_form.*
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*
import java.util.ArrayList


open class BaseBillingAddressFragment : BaseFragment()
{
    protected lateinit var model: CheckoutModel


    var isValidInfo = true

    protected var countryIdByForm: String = ""
    protected var stateProvinceIdByForm = ""

    private var billingAddressData = BillingAddressData()

    protected var isBillingAddressSubmitted = false

    protected var addressID: Long = 0

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_base_billing_adddress

    override fun getRootLayout(): RelativeLayout = baseBillingRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutAddressViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CheckoutModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CheckoutAddressViewModel::class.java)

        (viewModel as CheckoutAddressViewModel).getBillingFormVM(model)

        setAddressTabClickListener()

        setLiveDataListeners()

        btnContinue?.setOnClickListener {

            if(addressID == 0L)
            {
                getAddressWithValidation()

                if(isValidInfo)
                    (viewModel as CheckoutAddressViewModel).saveNewBillingVM(billingAddressData.billingAddress, model)
            }
            else
                (viewModel as CheckoutAddressViewModel).saveBillingFromExistingAddressVM(addressID, model)
        }


    }

    protected fun createNewAddressLayout(billingNewAddress: AddressModel)
    {
        createForms(billingNewAddress)

        val countryNameList = billingNewAddress.availableCountries?.map { it.text }
        populateCountrySpinner(countryNameList!!, billingNewAddress.availableCountries)

        newAddressLayout?.visibility = View.VISIBLE
    }

    open fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {
            billingAddressResponseLD.observe(viewLifecycleOwner, Observer { billingAddressResponse ->

                billingAddressData = billingAddressResponse.data

                with(billingAddressResponse.data.billingAddress)
                {

                    if(this.existingAddresses.isNotEmpty())
                        createAddressDropdown(this.existingAddresses)
                    else
                        createNewAddressLayout(this.billingNewAddress)

                    layoutBillingAddress?.visibility = View.VISIBLE
                }
            })

            stateListLD.observe(viewLifecycleOwner, Observer { stateList ->

                stateSpinner?.adapter = createSpinnerAdapter(stateList.map { it.name })

                stateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
                    {
                        if (position != 0)
                            stateProvinceIdByForm = "" + stateList[position - 1].id
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

                stateSpinnerLayout?.visibility = View.VISIBLE
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            saveBillingResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {
                    toast("Address Added Successfully")
                    isBillingAddressSubmitted = true
                    Handler().post {   addressTabLayout?.getTabAt(1)?.select()  }
                }




            })
        }
    }

    private fun createAddressDropdown(existingAddresses: List<AddressModel>)
    {
        val addressList = mutableListOf<String>()
        addressList.addAll(existingAddresses.map { "${it.firstName}, ${it.lastName}, ${it.address1},${it.city},${it.countryName}" })
        addressList.add("New Address")

        existingAddressSpinner?.adapter = createSpinnerAdapter(addressList)

        existingAddressSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) { onSelectAddressDropDown(existingAddresses, position) }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
        existingAddressLayout?.visibility = View.VISIBLE

    }

    private fun onSelectAddressDropDown(existingAddress: List<AddressModel>, position: Int)
    {
        if (isNewAddressSelected(existingAddress, position))
        {
            addressID = 0
            createNewAddressLayout(billingAddressData.billingAddress.billingNewAddress)
        }
        else
            onSelectExistingAddress(existingAddress, position)

    }
    protected fun onSelectExistingAddress(existingAddress: List<AddressModel>, position: Int)
    {
        addressID = existingAddress[position].id.toLong()
        newAddressLayout?.visibility = View.GONE
    }
    

    private fun isNewAddressSelected(existingAddress: List<AddressModel>?, position: Int): Boolean = (position == existingAddress!!.size)
    

    protected fun createSpinnerAdapter(nameList: List<String>): ArrayAdapter<String>
    {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, nameList)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        return dataAdapter
    }

    private fun populateCountrySpinner(countryNameList: List<String>, availableCountries: List<AvailableCountry>?)
    {

        countrySpinner?.adapter = createSpinnerAdapter(countryNameList)
        countrySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position != 0) {
                    stateProvinceIdByForm = ""
                    countryIdByForm = availableCountries!![position].value

                    (viewModel as CheckoutAddressViewModel).getStatesByCountryVM(countryIdByForm!!, model)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun createForms(newAddress: AddressModel)
    {
        with(newAddress)
        {

            etFirstName?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = firstName)

            etLastName?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = lastName)

            etEmail?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true, value = email)

            etCompanyName?.showOrHideOrRequired(isEnabledParam = companyEnabled, isRequired =   companyRequired)

            etStateProvince?.showOrHideOrRequired(isEnabledParam = stateProvinceEnabled, isRequired =   false)

            etCity?.showOrHideOrRequired(isEnabledParam = cityEnabled, isRequired =   cityRequired)

            etStreetAddress?.showOrHideOrRequired(isEnabledParam = streetAddressEnabled, isRequired =   streetAddressRequired)

            etStreetAddress2?.showOrHideOrRequired(isEnabledParam = streetAddress2Enabled, isRequired =   streetAddress2Required)

            etZipCode?.showOrHideOrRequired(isEnabledParam = zipPostalCodeEnabled, isRequired =   zipPostalCodeRequired)

            etPhone?.showOrHideOrRequired(isEnabledParam = phoneEnabled, isRequired =   phoneRequired)

            etFax?.showOrHideOrRequired(isEnabledParam = faxEnabled, isRequired =   faxRequired)

            etZipCode?.showOrHideOrRequired(isEnabledParam = zipPostalCodeEnabled, isRequired =   zipPostalCodeRequired)


        }

        rootScrollViewBillingAddress?.visibility = View.VISIBLE

    }

    private fun setAddressTabClickListener()
    {
        addressTabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab)
            {

                when(tab.position)
                {
                    Constants.BILLING_ADDRESS -> {
                        layoutBillingAddress.visibility = View.VISIBLE
                        layoutShippingAddress.visibility = View.GONE
                    }

                    Constants.SHIPPING_ADDRESS ->
                    {
                        if (isBillingAddressSubmitted)
                        {
                            layoutBillingAddress.visibility = View.GONE
                            layoutShippingAddress.visibility = View.VISIBLE
                        }
                        else
                        {
                            toast("Please complete previous step")
                            Handler().post {   addressTabLayout?.getTabAt(0)?.select()  }
                        }


                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
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


    protected fun getAddressWithValidation()
    {
        with(billingAddressData.billingAddress.billingNewAddress)
        {

            etCompanyName?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) company = input else { showValidation(it, companyRequired)}
            }

            etStreetAddress?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) address1 = input else { showValidation(it, streetAddressRequired) }
            }

            etStreetAddress2?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) address2 = input else { showValidation(it, streetAddress2Required) }
            }

            etZipCode?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) zipPostalCode = input else { showValidation(it, zipPostalCodeRequired)}
            }

            etCity?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) city = input else { showValidation(it, cityRequired) }
            }


            countryId = countryIdByForm

            availableCountries = null

            if(countyRequired && countryIdByForm.isEmpty())
            {
                isValidInfo = false
                toast("Country ${getString(R.string.reg_hint_is_required)}" )
            }

            stateProvinceId = stateProvinceIdByForm


            etPhone?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) phoneNumber = input else { showValidation(it, phoneRequired)}
            }

            etEmail?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()  && input.isEmailValid()) email = input else {showValidation(it, true)}
            }

            etLastName?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) lastName = input else { showValidation(it, true) }
            }

            etFirstName?.let {
                val input = it.text?.trim().toString(); if(input.isNotEmpty()) firstName = input else { showValidation(it, true) }
            }
        }
    }



}