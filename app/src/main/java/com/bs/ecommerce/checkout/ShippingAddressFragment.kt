package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.bs.ecommerce.checkout.model.data.AddressModel
import com.bs.ecommerce.checkout.model.data.CheckoutSaveResponse
import com.bs.ecommerce.checkout.model.data.ShippingAddressModel
import com.bs.ecommerce.checkout.model.data.Store
import com.bs.ecommerce.MyApplication
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*
import kotlinx.android.synthetic.main.fragment_billing_address_store_layout.*


class ShippingAddressFragment : BaseCheckoutAddressFragment()
{
    private var storeId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        addressTabLayout?.getTabAt(CheckoutConstants.SHIPPING_ADDRESS_TAB)?.select()

        MyApplication.checkoutSaveResponse?.let { initShippingLayout(it) }

        btnContinue?.setOnClickListener {

            if(storeCheckBox?.isChecked == true)
                saveStoreData()

            else if(addressID == 0L)
            {
                val newAddress: AddressModel? = getAddressWithValidation(newAddress)

                if(isValidInfo && newAddress!=null)
                    (viewModel as CheckoutViewModel).saveNewShippingVM(newAddress, getCustomAttributeValues(), model)
            }
            else
                (viewModel as CheckoutViewModel)
                    .saveShippingFromExistingAddressVM(type = CheckoutConstants.ShippingAddress, addressId = addressID, model = model)
        }

    }
    private fun saveStoreData()
    {
        (viewModel as CheckoutViewModel)
            .saveShippingFromExistingAddressVM(type = CheckoutConstants.StorePickUp, addressId = storeId, model = model)
    }

    private fun initShippingLayout(saveResponse: CheckoutSaveResponse)
    {

        saveResponse.data.shippingAddressModel?.apply {

            newAddress = shippingNewAddress

            if(this.allowPickupInStore)
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
            showShippingAddressUI(this)
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
                storeId = MyApplication.checkoutSaveResponse?.data
                    ?.shippingAddressModel?.pickupPoints?.get(position)?.id ?: 0
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {}
        }
    }

}