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

        model = CheckoutModelImpl()

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
                saveResponse!!.data.shippingAddressModel?.let {

                    MyApplication.checkoutSaveResponse?.data?.shippingAddressModel = it

                    replaceFragmentWithoutSavingState(ShippingAddressFragment())

                    MyApplication.previouslySelectedTab = R.id.menu_address
                }
            }
            CheckoutConstants.ShippingMethod ->
            {
                saveResponse!!.data.shippingMethodModel?.let {

                    MyApplication.checkoutSaveResponse?.data?.shippingMethodModel = it

                    replaceFragmentWithoutSavingState(ShippingMethodFragment())

                    MyApplication.previouslySelectedTab = R.id.menu_shipping
                }
            }
            CheckoutConstants.PaymentMethod ->
            {
                saveResponse!!.data.paymentMethodModel?.let {

                    MyApplication.checkoutSaveResponse?.data?.paymentMethodModel = it

                    replaceFragmentWithoutSavingState(PaymentMethodFragment())

                    MyApplication.previouslySelectedTab = R.id.menu_payment
                }
            }
            CheckoutConstants.PaymentInfo -> {

                saveResponse!!.data.paymentInfoModel?.let {

                    startActivityForResult(
                        Intent(requireActivity(), WebViewPaymentActivity::class.java)
                            .putExtra(CheckoutConstants.CHECKOUT_STEP, CheckoutConstants.PaymentInfo)
                            .putExtra(CheckoutConstants.PAYMENT_INFO_NAME, it.paymentViewComponentName),
                        CheckoutConstants.PAYMENT_INFO_RESULT)
                }

            }

            CheckoutConstants.RedirectToGateway ->
                startActivity(Intent(requireActivity(), WebViewPaymentActivity::class.java)
                    .putExtra(CheckoutConstants.CHECKOUT_STEP, CheckoutConstants.RedirectToGateway)
                )

            CheckoutConstants.ConfirmOrder -> replaceFragmentWithoutSavingState(ConfirmOrderFragment())

            CheckoutConstants.Completed ->
                startActivity(Intent(requireActivity(), ResultActivity::class.java)
                        .putExtra(CheckoutConstants.CHECKOUT_STEP, CheckoutConstants.Completed)
                        .putExtra(CheckoutConstants.ORDER_ID, "10")
                )


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

                    toast(saveResponse.message.toString())

                    goToNextStep(saveResponse)
                }

            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })
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
                if (CheckoutStepFragment.isBillingAddressSubmitted &&
                    MyApplication.getBillingResponse?.data?.shippingRequired!! &&
                    MyApplication.checkoutSaveResponse?.data?.shippingAddressModel != null
                )
                {
                    if(!isCurrentTabShipping())
                        replaceFragmentWithoutSavingState(ShippingAddressFragment())

                }
                else
                {
                    toast(getString(R.string.please_complete_previous_step))
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

}