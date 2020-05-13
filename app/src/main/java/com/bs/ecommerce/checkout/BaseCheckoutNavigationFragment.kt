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

        setAddressTabClickListener()


    }
    open fun setLiveDataListeners() {

        with(viewModel as CheckoutAddressViewModel)
        {

            saveResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {

                    toast("Added Successfully")

                    MyApplication.checkoutSaveResponse = saveResponse

                    CheckoutStepFragment.isBillingAddressSubmitted = true

                    when(saveResponse.data.nextStep)
                    {

                        Constants.ShippingAddress -> {

                            addressTabLayout?.getTabAt(Constants.SHIPPING_ADDRESS_TAB)?.select()
                        }
                        Constants.ShippingMethod -> replaceFragment(ShippingMethodFragment())
                        Constants.PaymentMethod -> (parentFragment as CheckoutStepFragment).replaceFragment(PaymentMethodFragment())
                        Constants.PaymentInfo -> (parentFragment as CheckoutStepFragment).replaceFragment(ConfirmOrderFragment())
                    }


                }

            })
        }
    }

    private fun manualTabSelection(tabPosition: Int)
    {
        when(tabPosition)
        {
            Constants.BILLING_ADDRESS_TAB -> layoutCheckoutAddress?.visibility = View.VISIBLE

            Constants.SHIPPING_ADDRESS_TAB ->
            {
                if (CheckoutStepFragment.isBillingAddressSubmitted)
                {
                    replaceFragmentWithoutPendingTransactions(ShippingAddressFragment())

                }
                else
                {
                    toast("Please complete previous step")
                    Handler().post {   addressTabLayout?.getTabAt(Constants.BILLING_ADDRESS_TAB)?.select()  }
                }

                "sfdsgsdgd".showLog(CheckoutStepFragment.isBillingAddressSubmitted.toString())
            }
        }
    }


    private fun setAddressTabClickListener()
    {
        addressTabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                manualTabSelection(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab)
            {}
        })
    }

    private fun replaceFragment(fragment: BaseFragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        //transaction.commit()

        if (requireActivity().supportFragmentManager.isStateSaved)
        {
            transaction.commit()
            childFragmentManager.executePendingTransactions()
        }

    }

    private fun replaceFragmentWithoutPendingTransactions(fragment: BaseFragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        transaction.commit()

        /*if (requireActivity().supportFragmentManager.isStateSaved)
        {
            transaction.commit()
            //childFragmentManager.executePendingTransactions()
        }*/

        //replaceFragmentSafely(ShippingAddressFragment())

    }

    protected fun isCurrentTabShipping() : Boolean
            = requireActivity().supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is ShippingAddressFragment

}