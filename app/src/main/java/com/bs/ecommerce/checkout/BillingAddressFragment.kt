package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BillingAddressFragment : BaseCheckoutAddressFragment()
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        addressTabLayout?.getTabAt(CheckoutConstants.BILLING_ADDRESS_TAB)?.select()


        if(MyApplication.billingNewAddressSaved)
            updateAddressWhenNewAdded()
        else
            populateAllData()


        btnContinue?.setOnClickListener {

            if(addressID == 0L)
            {
                val newAddress = getAddressWithValidation(newAddress)

                if(isValidInfo && newAddress!=null && getCustomAttributeValues()!=null)
                    (viewModel as CheckoutViewModel).saveNewBillingVM(newAddress, getCustomAttributeValues()!!, model)
            }
            else
                (viewModel as CheckoutViewModel).saveExistingBillingVM(addressID, model, shipToSameAddressCheckBox.isChecked)
        }
    }

    private fun populateAllData()
    {
        MyApplication.getBillingResponse?.data?.billingAddress?.billingNewAddress?.let { newAddress = it }
        MyApplication.getBillingResponse?.let { showBillingAddressUI(it) }
    }


    private fun updateAddressWhenNewAdded()
    {
        if(MyApplication.billingNewAddressSaved)
        {
            MyApplication.getBillingResponse = null

            MyApplication.billingNewAddressSaved = false

            (viewModel as CheckoutViewModel).apply {

                getBillingFormVM(model)

                getBillingAddressLD.observe(viewLifecycleOwner, Observer { getBilling ->

                    MyApplication.getBillingResponse = getBilling
                    populateAllData()
                })
            }
        }
    }

    private fun showBillingAddressUI(billingAddressResponse: BillingAddressResponse)
    {
        with(billingAddressResponse.data.billingAddress)
        {

            if(shipToSameAddressAllowed)
            {
                shipToSameAddressCheckBox?.visibility = View.VISIBLE

                if(shipToSameAddress)
                    shipToSameAddressCheckBox.isChecked = true
            }

            if(this.existingAddresses.isEmpty() || newAddressPreselected)
                createNewAddressLayout(this.billingNewAddress)
            else
                createAddressDropdown(this.existingAddresses)

        }
    }

}