package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.BillingNewAddress
import com.bs.ecommerce.utils.showOrHideOrRequired
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.address_form.*
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BaseBillingAddressFragment : BaseFragment()
{
    private lateinit var model: CheckoutModel

    lateinit var keyPrefixTag: String

    protected var countryCode: String? = ""
    protected var StateProvinceCode = ""
    var addressID: Long = 0

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_base_billing_adddress

    override fun getRootLayout(): RelativeLayout = baseBillingRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutAddressViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initView()

        model = CheckoutModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CheckoutAddressViewModel::class.java)

        (viewModel as CheckoutAddressViewModel).getBillingFormVM(model)

        setLiveDataListeners()


    }

    private fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {
            billingAddressResponseLD.observe(viewLifecycleOwner, Observer { billingAddressResponse ->

                with(billingAddressResponse.data.billingAddress)
                {
                    createForms(this.billingNewAddress)

                    val countryNameList = billingNewAddress.availableCountries.map { it.text }
                    populateCountrySpinner(countryNameList, billingNewAddress.availableCountries)

                    layoutBillingAddress?.visibility =View.VISIBLE
                }
            })

            stateListLD.observe(viewLifecycleOwner, Observer { stateList ->

                stateSpinner?.adapter = createSpinnerAdapter(stateList.map { it.name })

                stateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
                    {
                        if (position != 0)
                            StateProvinceCode = "" + stateList[position - 1].id
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

                stateSpinnerLayout?.visibility =View.VISIBLE
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    private fun createSpinnerAdapter(nameList: List<String>): ArrayAdapter<String>
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
                    StateProvinceCode = ""
                    countryCode = availableCountries!![position].value

                    (viewModel as CheckoutAddressViewModel).getStatesByCountryVM(countryCode!!, model)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun createForms(newAddress: BillingNewAddress)
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

    private fun initView() {
        billingTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (tab.position == 0) {
                    layoutBillingAddress.visibility = View.VISIBLE
                    layoutShippingAddress.visibility = View.GONE
                } else if (tab.position == 1) {
                    layoutBillingAddress.visibility = View.GONE
                    layoutShippingAddress.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


}