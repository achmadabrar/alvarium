package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BillingAddressFragment : BaseCheckoutAddressFragment()
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as CheckoutAddressViewModel).getBillingFormVM(model)


        btnContinue?.setOnClickListener {

            if(addressID == 0L)
            {
                val newAddress = getAddressWithValidation(newAddress!!)

                if(isValidInfo)
                    (viewModel as CheckoutAddressViewModel).saveNewBillingVM(newAddress, model)
            }
            else
                (viewModel as CheckoutAddressViewModel).saveExistingBillingVM(addressID, model, shipToSameAddressCheckBox.isChecked)
        }

    }


    override fun setLiveDataListeners() {

        super.setLiveDataListeners()

        with(viewModel as CheckoutAddressViewModel)
        {
            getBillingAddressLD.observe(viewLifecycleOwner, Observer { getBilling ->

                newAddress = getBilling.data.billingAddress.billingNewAddress

                showBillingAddressUI(getBilling)

            })


        }
    }
    protected fun showBillingAddressUI(billingAddressResponse: BillingAddressResponse)
    {
        with(billingAddressResponse.data.billingAddress)
        {
            if(this.existingAddresses.isNotEmpty())
                createAddressDropdown(this.existingAddresses)
            else
                createNewAddressLayout(this.billingNewAddress)

            layoutCheckoutAddress?.visibility = View.VISIBLE

            shipToSameAddressCheckBox?.visibility = View.VISIBLE

        }
    }

}