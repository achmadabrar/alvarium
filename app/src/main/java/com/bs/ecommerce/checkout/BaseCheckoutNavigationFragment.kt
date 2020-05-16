package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.utils.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*


abstract class BaseCheckoutNavigationFragment : BaseFragment()
{

    protected lateinit var model: CheckoutModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CheckoutModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CheckoutAddressViewModel::class.java)

        setLiveDataListeners()

        setAddressTabClickListener()

        setLiveDataListeners()


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

                            replaceFragmentWithoutSavingState(ShippingAddressFragment())
                        }
                        Constants.ShippingMethod -> {

                            replaceFragmentWithoutSavingState(ShippingMethodFragment())
                        }

                        Constants.PaymentMethod -> {
                            replaceFragmentWithoutSavingState(PaymentMethodFragment())
                        }
                        Constants.PaymentInfo -> replaceFragmentWithoutSavingState(ConfirmOrderFragment())
                    }


                }

            })
        }
    }

    private fun manualTabSelection(tabPosition: Int)
    {
        when(tabPosition)
        {
            Constants.BILLING_ADDRESS_TAB ->
            {
                if(!isCurrentTabBilling())
                    replaceFragmentWithoutSavingState(BillingAddressFragment())
            }

            Constants.SHIPPING_ADDRESS_TAB ->
            {
                if (CheckoutStepFragment.isBillingAddressSubmitted)
                {
                    if(!isCurrentTabShipping())
                        replaceFragmentWithoutSavingState(ShippingAddressFragment())

                }
                else
                {
                    toast("Please complete previous step")
                    Handler().post {   addressTabLayout?.getTabAt(Constants.BILLING_ADDRESS_TAB)?.select()  }
                }
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


    private fun replaceFragmentWithoutSavingState(fragment: BaseFragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        transaction.commit()

        childFragmentManager.executePendingTransactions()


        val csFragment = requireActivity().supportFragmentManager.
                findFragmentByTag(CheckoutStepFragment::class.java.simpleName)

        if(csFragment is CheckoutStepFragment) csFragment.updateBottomNavItem(fragment)
    }

    private fun isCurrentTabShipping() : Boolean
            = requireActivity().supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is ShippingAddressFragment

    private fun isCurrentTabBilling() : Boolean
            = requireActivity().supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is BillingAddressFragment

}