package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*
import kotlinx.android.synthetic.main.fragment_billing_address.btnContinue
import kotlinx.android.synthetic.main.fragment_billing_address_store_layout.*
import kotlinx.android.synthetic.main.fragment_shipping_address.*


class ShippingAddressFragment : BaseBillingAddressFragment()
{
    private var storeId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initShippingLayout(MyApplication.saveBillingResponse!!)

    }
    override fun saveStoreData()
    {

        (viewModel as CheckoutAddressViewModel)
            .saveShippingFromExistingAddressVM(type = Constants.StorePickUp, addressId = addressID, model = model)
    }

    override fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {

            saveBillingResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {
                    toast("Address Added Successfully")
                    MyApplication.saveBillingResponse = saveResponse
                    CheckoutStepFragment.isShippingAddressSubmitted = true

                    Handler().post {   addressTabLayout?.getTabAt(Constants.SHIPPING_ADDRESS_TAB)?.select()  }
                }

            })
        }
    }

    private fun initShippingLayout(saveBillingResponse: SaveBillingResponse)
    {

        with(saveBillingResponse.data.shippingAddressModel)
        {

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

            storeLayout.visibility = View.VISIBLE

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
                storeId = MyApplication.saveBillingResponse!!.data.shippingAddressModel.pickupPoints[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {}
        }
    }

}