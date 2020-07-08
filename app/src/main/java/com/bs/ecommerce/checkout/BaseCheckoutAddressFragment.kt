package com.bs.ecommerce.checkout

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.data.AddressModel
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.address_form.*
import kotlinx.android.synthetic.main.address_form.view.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


open class BaseCheckoutAddressFragment : BaseCheckoutNavigationFragment()
{

    var isValidInfo = true

    protected var countryIdByForm: String = ""
    protected var stateProvinceIdByForm = ""

    protected var newAddress : AddressModel? = null

    protected var addressID: Long = 0

    private var customAttributeManager: CustomAttributeManager? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_base_billing_adddress

    override fun getRootLayout(): RelativeLayout = baseBillingRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()


    override fun setLiveDataListeners() {

        super.setLiveDataListeners()

        with(viewModel as CheckoutViewModel)
        {

            stateListLD.observe(viewLifecycleOwner, Observer { stateList ->

                val stateListToShow= mutableListOf<String>()
                stateListToShow.add(DbHelper.getString(Const.SELECT_STATE))

                stateListToShow.addAll(stateList.map { it.name } as MutableList<String>)

                stateSpinner?.adapter = createSpinnerAdapter(stateListToShow)

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
        }
        setLanguageStrings()
    }

    private fun setLanguageStrings()
    {
        addressTabLayout?.getTabAt(CheckoutConstants.BILLING_ADDRESS_TAB)?.text = DbHelper.getString(Const.BILLING_ADDRESS_TAB)
        addressTabLayout?.getTabAt(CheckoutConstants.SHIPPING_ADDRESS_TAB)?.text = DbHelper.getString(Const.SHIPPING_ADDRESS_TAB)

        shipToSameAddressCheckBox?.text = DbHelper.getString(Const.SHIP_TO_SAME_ADDRESS)

        btnContinue?.text = DbHelper.getString(Const.CONTINUE)

    }



    protected fun createNewAddressLayout(address: AddressModel)
    {
        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutCheckout)

        // setup product attributes
        customAttributeManager =
            CustomAttributeManager(
                activity = requireActivity(),
                attributes = address.customAddressAttributes,
                attributeViewHolder = layoutCheckoutAddress.customAttributeViewHolder,
                attributeValueHolder = bottomSheetLayoutCheckout.attributeValueHolder,
                bsBehavior = bsBehavior
            )

        customAttributeManager?.attachAttributesToFragment()

        createForms(address)

        val countryNameList = address.availableCountries?.map { it.text }

        countryNameList?.let {  populateCountrySpinner(countryNameList, address.availableCountries)  }

        android.os.Handler().post {  newAddressLayout?.visibility = View.VISIBLE    }

    }

    protected fun getCustomAttributeValues(): KeyValueFormData {

        return customAttributeManager
            ?.getFormData(Api.addressAttributePrefix) ?: KeyValueFormData()
    }


    protected fun createAddressDropdown(existingAddresses: List<AddressModel>)
    {
        val addressList = mutableListOf<String>()
        addressList.addAll(existingAddresses.map { "${it.firstName}, ${it.lastName}, ${it.address1},${it.city},${it.countryName}" })
        addressList.reverse()
        addressList.add(DbHelper.getString(Const.NEW_ADDRESS))

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
            newAddress?.let { createNewAddressLayout(it) }
        }
        else
            onSelectExistingAddress(existingAddress, position)

    }
    protected fun onSelectExistingAddress(existingAddress: List<AddressModel>, position: Int)
    {
        addressID = existingAddress[position].id.toLong()
        newAddressLayout?.visibility = View.GONE
    }
    

    private fun isNewAddressSelected(existingAddress: List<AddressModel>?, position: Int): Boolean
            = (position == existingAddress?.size ?: 0)
    

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
                    countryIdByForm = availableCountries?.get(position)?.value ?: ""

                    (viewModel as CheckoutViewModel).getStatesByCountryVM(countryIdByForm, model)
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

            etFirstName?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = firstName,
                hintText = DbHelper.getString(Const.FIRST_NAME))

            etLastName?.showOrHideOrRequired(isEnabledParam = true, isRequired =   false, value = lastName,
                    hintText = DbHelper.getString(Const.LAST_NAME))

            etEmail?.showOrHideOrRequired(isEnabledParam = true, isRequired =   true, value = email,
                hintText = DbHelper.getString(Const.EMAIL))

            etCompanyName?.showOrHideOrRequired(isEnabledParam = companyEnabled, isRequired =   companyRequired,
                hintText = DbHelper.getString(Const.COMPANY))

            etCity?.showOrHideOrRequired(isEnabledParam = cityEnabled, isRequired =   cityRequired,
                hintText = DbHelper.getString(Const.CITY))

            etStreetAddress?.showOrHideOrRequired(isEnabledParam = streetAddressEnabled, isRequired =   streetAddressRequired,
                hintText = DbHelper.getString(Const.STREET_ADDRESS))

            etStreetAddress2?.showOrHideOrRequired(isEnabledParam = streetAddress2Enabled, isRequired =   streetAddress2Required,
                hintText = DbHelper.getString(Const.STREET_ADDRESS_2))

            etZipCode?.showOrHideOrRequired(isEnabledParam = zipPostalCodeEnabled, isRequired =   zipPostalCodeRequired,
                hintText = DbHelper.getString(Const.ZIP_CODE))

            etPhone?.showOrHideOrRequired(isEnabledParam = phoneEnabled, isRequired =   phoneRequired,
                hintText = DbHelper.getString(Const.PHONE))

            etFax?.showOrHideOrRequired(isEnabledParam = faxEnabled, isRequired =   faxRequired,
                hintText = DbHelper.getString(Const.FAX))

        }

        rootScrollViewBillingAddress?.visibility = View.VISIBLE

    }


    private fun showValidation( editText: EditText, isRequired: Boolean)
    {
        if(isRequired)
        {
            isValidInfo = false
            toast("${editText.hint} ${DbHelper.getString(Const.IS_REQUIRED)}" )
        }
        else
            isValidInfo = true

    }


    protected fun getAddressWithValidation(address: AddressModel?) : AddressModel?
    {
        address?.apply {

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
                toast("Country ${DbHelper.getString(Const.IS_REQUIRED)}" )
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

        return address
    }

}