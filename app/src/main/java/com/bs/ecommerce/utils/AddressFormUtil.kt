package com.bs.ecommerce.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.bs.ecommerce.R
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.catalog.common.AddressModel
import kotlinx.android.synthetic.main.address_form.view.*

class AddressFormUtil(private val form: View, private val context: Context) {

    private var isValidForm: Boolean = true
    var validAddress: AddressModel? = null
        private set

    fun populateCountrySpinner(
        availableCountries: List<AvailableCountry>,
        itemClickListener: ItemClickListener<AvailableCountry>
    ) {

        val countryAdapter: ArrayAdapter<AvailableCountry> =
            ArrayAdapter<AvailableCountry>(context, R.layout.simple_spinner_item, availableCountries)

        countryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        form.countrySpinner?.adapter = countryAdapter

        form.countrySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) = itemClickListener.onClick(view, position, availableCountries[position])


            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // set selected country
        availableCountries.indexOfFirst { it.selected }.also {
            if (it >= 0) form.countrySpinner.setSelection(it)
        }
    }

    fun prepareEditText(address: AddressModel) {
        with(address)
        {
            form.etFirstName?.showOrHideOrRequired(
                isEnabledParam = true,
                isRequired = false,
                value = firstName,
                hintText = DbHelper.getString(Const.FIRST_NAME)
            )

            form.etLastName?.showOrHideOrRequired(
                isEnabledParam = true,
                isRequired = false,
                value = lastName,
                hintText = DbHelper.getString(Const.LAST_NAME)
            )

            form.etEmail?.showOrHideOrRequired(
                isEnabledParam = true,
                isRequired = true,
                value = email,
                hintText = DbHelper.getString(Const.EMAIL)
            )

            form.etCompanyName?.showOrHideOrRequired(
                isEnabledParam = companyEnabled == true,
                isRequired = companyRequired == true,
                value = company,
                hintText = DbHelper.getString(Const.COMPANY)
            )

/*            form.etStateProvince?.showOrHideOrRequired(
                isEnabledParam = stateProvinceEnabled == true,
                isRequired = false,
                hintText = DbHelper.getString(Const.STATE_PROVINCE)
            )*/

            form.etCity?.showOrHideOrRequired(
                isEnabledParam = cityEnabled == true,
                isRequired = cityRequired == true,
                value = city,
                hintText = DbHelper.getString(Const.CITY)
            )

            form.etStreetAddress?.showOrHideOrRequired(
                isEnabledParam = streetAddressEnabled == true,
                isRequired = streetAddressRequired == true,
                value = address1,
                hintText = DbHelper.getString(Const.STREET_ADDRESS)
            )

            form.etStreetAddress2?.showOrHideOrRequired(
                isEnabledParam = streetAddress2Enabled == true,
                isRequired = streetAddress2Required == true,
                value = address2,
                hintText = DbHelper.getString(Const.STREET_ADDRESS_2)
            )

            form.etZipCode?.showOrHideOrRequired(
                isEnabledParam = zipPostalCodeEnabled == true,
                isRequired = zipPostalCodeRequired == true,
                value = zipPostalCode,
                hintText = DbHelper.getString(Const.ZIP_CODE)
            )

            form.etPhone?.showOrHideOrRequired(
                isEnabledParam = phoneEnabled == true,
                isRequired = phoneRequired == true,
                value = phoneNumber,
                hintText = DbHelper.getString(Const.PHONE)
            )

            form.etFax?.showOrHideOrRequired(
                isEnabledParam = faxEnabled == true,
                isRequired = faxRequired == true,
                value = faxNumber,
                hintText = DbHelper.getString(Const.FAX)
            )

        }

    }

    /**
     * @return Address object with form data, which will be used as RequestBody of SaveAddress api
     */
    fun isFormDataValid(address: AddressModel): Boolean {
        with(address)
        {

            form.etCompanyName?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) company =
                input else {
                showValidation(it, companyRequired)
            }
            }

            form.etStreetAddress?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) address1 =
                input else {
                showValidation(it, streetAddressRequired)
            }
            }

            form.etStreetAddress2?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) address2 =
                input else {
                showValidation(it, streetAddress2Required)
            }
            }

            form.etZipCode?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) zipPostalCode =
                input else {
                showValidation(it, zipPostalCodeRequired)
            }
            }

            form.etCity?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) city = input else {
                showValidation(it, cityRequired)
            }
            }


            countryId = null
            if (form.countrySpinner.selectedItem is AvailableCountry)
                countryId = (form.countrySpinner.selectedItem as AvailableCountry).value.toInt()

            availableCountries = null

            if (countryEnabled==true && countryId == 0) {
                isValidForm = false
                Toast.makeText(context, DbHelper.getString(Const.COUNTRY_REQUIRED), Toast.LENGTH_SHORT).show()
            }

            stateProvinceId = null
            if (form.stateSpinner.selectedItem is AvailableState)
                stateProvinceId = (form.stateSpinner.selectedItem as AvailableState).id


            form.etPhone?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) phoneNumber =
                input else {
                showValidation(it, phoneRequired)
            }
            }

            form.etEmail?.let {
                val input = it.text?.trim()
                    .toString(); if (input.isNotEmpty() && input.isEmailValid()) email =
                input else {
                showValidation(it, true)
            }
            }

            form.etLastName?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) lastName =
                input else {
                showValidation(it, true)
            }
            }

            form.etFirstName?.let {
                val input = it.text?.trim().toString(); if (input.isNotEmpty()) firstName =
                input else {
                showValidation(it, true)
            }
            }
        }

        validAddress = address

        return isValidForm
    }

    private fun showValidation(editText: EditText, isRequired: Boolean?) {
        if (isRequired == true) {
            isValidForm = false
            val toastMsg = "${editText.hint} ${DbHelper.getString(Const.IS_REQUIRED)}"

            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        } else {
            isValidForm = true
        }
    }

    fun populateStateSpinner(selectedStateId: Int?, states: List<AvailableState>) {

        val spinnerAdapter: ArrayAdapter<AvailableState> =
            ArrayAdapter<AvailableState>(context, R.layout.simple_spinner_item, states)

        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        form.stateSpinner?.adapter = spinnerAdapter

        form.stateSpinnerLayout?.visibility = View.VISIBLE

        // set selected country
        states.indexOfFirst { it.id == selectedStateId }.also {
            if (it >= 0) form.stateSpinner.setSelection(it)
        }
    }
}