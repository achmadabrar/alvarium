package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BillingAddressFragment : BaseCheckoutAddressFragment()
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as CheckoutAddressViewModel).getBillingFormVM(model)

        setLiveDataListeners()

        btnContinue?.setOnClickListener {

            if(addressID == 0L)
            {
                val newAddress = getAddressWithValidation(newAddress!!)

                if(isValidInfo)
                    (viewModel as CheckoutAddressViewModel).saveNewBillingVM(newAddress, model)
            }
            else
                (viewModel as CheckoutAddressViewModel).saveBillingFromExistingAddressVM(addressID, model)
        }

    }


    override fun setLiveDataListeners() {

        super.setLiveDataListeners()

        with(viewModel as CheckoutAddressViewModel)
        {
            billingAddressResponseLD.observe(viewLifecycleOwner, Observer { billingAddressResponse ->

                newAddress = billingAddressResponse.data.billingAddress.billingNewAddress

                showBillingAddressUI(billingAddressResponse)

            })

            saveBillingResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {

                    toast("Address Added Successfully")

                    MyApplication.saveBillingResponse = saveResponse

                    CheckoutStepFragment.isBillingAddressSubmitted = true

                    Handler().post {   addressTabLayout?.getTabAt(Constants.SHIPPING_ADDRESS_TAB)?.select()  }

                }

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

        }
    }

}