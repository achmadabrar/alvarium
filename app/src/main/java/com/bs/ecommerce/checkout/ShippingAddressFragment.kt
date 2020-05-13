package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.checkout.model.data.CheckoutSaveResponse
import com.bs.ecommerce.checkout.model.data.ShippingAddressModel
import com.bs.ecommerce.checkout.model.data.Store
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*
import kotlinx.android.synthetic.main.fragment_billing_address_store_layout.*


class ShippingAddressFragment : BaseCheckoutAddressFragment()
{
    private var storeId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initShippingLayout(MyApplication.checkoutSaveResponse!!)

        btnContinue?.setOnClickListener {

            if(storeCheckBox?.isChecked!!)
                saveStoreData()

            else if(addressID == 0L)
            {
                val newAddress = getAddressWithValidation(newAddress!!)

                if(isValidInfo)
                    (viewModel as CheckoutAddressViewModel).saveNewShippingVM(newAddress, model)
            }
            else
                (viewModel as CheckoutAddressViewModel)
                    .saveShippingFromExistingAddressVM(type = Constants.ShippingAddress, addressId = addressID, model = model)
        }

    }
    private fun saveStoreData()
    {
        (viewModel as CheckoutAddressViewModel)
            .saveShippingFromExistingAddressVM(type = Constants.StorePickUp, addressId = addressID, model = model)
    }

    private fun initShippingLayout(saveBillingResponse: CheckoutSaveResponse)
    {

        with(saveBillingResponse.data.shippingAddressModel)
        {

            newAddress = shippingNewAddress

            if(saveBillingResponse.data.shippingAddressModel.allowPickupInStore)
                storeLayout?.visibility = View.VISIBLE
            else
                storeLayout?.visibility = View.GONE


            storeCheckBox?.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked)
                {
                    generateStoreDropdownList(pickupPoints)
                    newAddressLayout.visibility = View.GONE

                    storeSpinnerLayout?.visibility = View.VISIBLE
                    existingAddressLayout?.visibility = View.GONE
                }
                else
                {
                    createAddressDropdown(existingAddresses)

                    storeSpinnerLayout?.visibility = View.GONE
                    existingAddressLayout?.visibility = View.VISIBLE
                }
            }
            showShippingAddressUI(saveBillingResponse.data.shippingAddressModel)
        }

    }

    private fun showShippingAddressUI(shippingAddressModel: ShippingAddressModel)
    {
        with(shippingAddressModel)
        {
            if(existingAddresses.isNotEmpty())
                createAddressDropdown(existingAddresses)
            else
                createNewAddressLayout(shippingNewAddress)

            layoutCheckoutAddress?.visibility = View.VISIBLE
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
                storeId = MyApplication.checkoutSaveResponse!!.data.shippingAddressModel.pickupPoints[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {}
        }
    }

}