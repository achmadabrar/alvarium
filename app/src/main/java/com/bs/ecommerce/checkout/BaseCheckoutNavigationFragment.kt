package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.address_form.*
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


abstract class BaseCheckoutNavigationFragment : BaseFragment()
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setLiveDataListeners()


    }
    open fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {

            saveBillingResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {

                    toast("Added Successfully")

                    MyApplication.saveBillingResponse = saveResponse

                    CheckoutStepFragment.isBillingAddressSubmitted = true


                    when(saveResponse.data.nextStep)
                    {
                        Constants.ShippingAddress -> Handler().post {   addressTabLayout?.getTabAt(Constants.SHIPPING_ADDRESS_TAB)?.select()  }
                        //Constants.ShippingMethod -> (parentFragment as CheckoutStepFragment).replaceFragment(ShippingMethodFragment())
                        //Constants.PaymentMethod -> (parentFragment as CheckoutStepFragment).replaceFragment(PaymentMethodFragment())
                        Constants.PaymentInfo -> (parentFragment as CheckoutStepFragment).replaceFragment(ConfirmOrderFragment())
                    }


                }

            })
        }
    }

}