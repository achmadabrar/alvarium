package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.checkout.model.data.BillingAddressData
import com.bs.ecommerce.checkout.model.data.SaveBillingResponse
import com.bs.ecommerce.checkout.model.data.Store
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_billing_address.btnContinue
import kotlinx.android.synthetic.main.fragment_shipping_address.*
import java.util.ArrayList


class ShippingAddressFragment : BaseBillingAddressFragment()
{

    private var billingAddressData = BillingAddressData()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


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

    private fun initShippingLayout(saveBillingResponse: SaveBillingResponse)
    {

        with(saveBillingResponse.data.shippingAddressModel)
        {
            if(saveBillingResponse.data.shippingAddressModel.allowPickupInStore)
            {
                storeLayout?.visibility = View.VISIBLE
            }
            else
            {
                storeLayout?.visibility = View.GONE
            }


            storeCheckBox?.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked)
                {
                    generateStoreDropdownList(pickupPoints)
                    shippingAddressLayout.visibility = View.GONE
                }
                else
                {
                    shippingAddressLayout.visibility = View.VISIBLE
                }
            }

        }



    }
    private fun generateStoreDropdownList(storeList: List<Store>)
    {

        val storeListForSpinner = storeList.map { "${it.name} ${it.address}, ${it.city}, ${it.countryName}, ${it.pickupFee}" }

        storeSpinner?.adapter = createSpinnerAdapter(storeListForSpinner)


        storeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
            {
                //storeId = storeAddressResponse?.pickupPoints!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {}
        }
    }

    override fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {

            saveBillingResponseLD.observe(viewLifecycleOwner, Observer { saveBillingResponse ->

                if(saveBillingResponse.errorList.isNotEmpty())
                    toast(saveBillingResponse.errorsAsFormattedString)
                else
                {
                    toast(saveBillingResponse.message)

                    initShippingLayout(saveBillingResponse)
                }


            })
        }
    }

}