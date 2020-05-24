package com.bs.ecommerce.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.CheckoutSaveResponse
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*


abstract class BaseCheckoutNavigationFragment : ToolbarLogoBaseFragment()
{

    protected lateinit var model: CheckoutModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CheckoutModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        setLiveDataListeners()

        setAddressTabClickListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CheckoutConstants.PAYMENT_INFO_RESULT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val nextStepId = data?.getIntExtra(CheckoutConstants.NEXT_STEP_AFTER_PAYMENT_INFO, 0)
                goToNextStep(stepIdFromPaymentInfoPage = nextStepId)
            }
        }
    }

    private fun goToNextStep(saveResponse: CheckoutSaveResponse? = null, stepIdFromPaymentInfoPage: Int? = null)
    {
        var nextStepId = 0

        if(stepIdFromPaymentInfoPage != null)
            nextStepId = stepIdFromPaymentInfoPage
        else
            nextStepId = saveResponse?.data?.nextStep!!

        when(nextStepId)
        {
            CheckoutConstants.ShippingAddress ->
            {

                /*if(saveResponse.data.nextStep > 0)
                {
                    val previousStep = saveResponse.data.nextStep - 1

                    for(anyStep in 0 until 7)
                    {
                        if(previousStep == anyStep)
                        {
                            when(previousStep)
                            {
                                CheckoutConstants.ShippingAddress
                                CheckoutConstants.ShippingMethod
                                CheckoutConstants.PaymentMethod
                                CheckoutConstants.PaymentInfo
                                 CheckoutConstants.ConfirmOrder
                                CheckoutConstants.RedirectToGateway

                            }
                        }

                    }
                }*/


                MyApplication.checkoutSaveResponse.data.shippingAddressModel = saveResponse!!.data.shippingAddressModel

                //toast("Billing Address added Successfully")

                replaceFragmentWithoutSavingState(ShippingAddressFragment())
            }
            CheckoutConstants.ShippingMethod ->
            {
                MyApplication.checkoutSaveResponse.data.shippingMethodModel = saveResponse!!.data.shippingMethodModel

                //toast("Shipping Address added Successfully")

                replaceFragmentWithoutSavingState(ShippingMethodFragment())
            }
            CheckoutConstants.PaymentMethod ->
            {
                MyApplication.checkoutSaveResponse.data.paymentMethodModel = saveResponse!!.data.paymentMethodModel

                //toast("Shipping Method added Successfully")

                replaceFragmentWithoutSavingState(PaymentMethodFragment())
            }
            CheckoutConstants.PaymentInfo -> {

                //toast("Payment Method added Successfully")
                //replaceFragmentWithoutSavingState(PaymentInfoFragment())
                startActivityForResult(Intent(requireActivity(), PaymentInfoActivity::class.java), CheckoutConstants.PAYMENT_INFO_RESULT)
            }

            CheckoutConstants.ConfirmOrder -> replaceFragmentWithoutSavingState(ConfirmOrderFragment())
        }
    }


    open fun setLiveDataListeners() {

        with(viewModel as CheckoutViewModel)
        {

            saveResponseLD.observe(viewLifecycleOwner, Observer { saveResponse ->

                if(saveResponse.errorList.isNotEmpty())
                    toast(saveResponse.errorsAsFormattedString)
                else
                {
                    CheckoutStepFragment.isBillingAddressSubmitted = true

                    goToNextStep(saveResponse)
                }

            })
        }
    }

    private fun manualTabSelection(tabPosition: Int)
    {
        when(tabPosition)
        {
            CheckoutConstants.BILLING_ADDRESS_TAB ->
            {
                if(!isCurrentTabBilling())
                    replaceFragmentWithoutSavingState(BillingAddressFragment())
            }

            CheckoutConstants.SHIPPING_ADDRESS_TAB ->
            {
                if (CheckoutStepFragment.isBillingAddressSubmitted)
                {
                    if(!isCurrentTabShipping())
                        replaceFragmentWithoutSavingState(ShippingAddressFragment())

                }
                else
                {
                    toast("Please complete previous step")
                    Handler().post {   addressTabLayout?.getTabAt(CheckoutConstants.BILLING_ADDRESS_TAB)?.select()  }
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


    companion object{
        @JvmStatic var backNavigation = false
    }
}