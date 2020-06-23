package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.utils.MyApplication
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BillingAddressFragment : BaseCheckoutAddressFragment()
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        addressTabLayout?.getTabAt(CheckoutConstants.BILLING_ADDRESS_TAB)?.select()

        MyApplication.getBillingResponse?.data?.billingAddress?.billingNewAddress?.let { newAddress = it }

        btnContinue?.setOnClickListener {

            if(addressID == 0L)
            {
                val newAddress = getAddressWithValidation(newAddress)

                if(isValidInfo && newAddress!=null)
                    (viewModel as CheckoutViewModel).saveNewBillingVM(newAddress, getCustomAttributeValues(), model)
            }
            else
                (viewModel as CheckoutViewModel).saveExistingBillingVM(addressID, model, shipToSameAddressCheckBox.isChecked)
        }

        MyApplication.getBillingResponse?.let { showBillingAddressUI(it) }
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