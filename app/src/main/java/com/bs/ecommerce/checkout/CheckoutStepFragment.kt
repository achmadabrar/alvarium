package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class CheckoutStepFragment : ToolbarLogoBaseFragment() {


    private lateinit var model: CheckoutModel

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_checkout_step

    override fun getRootLayout(): RelativeLayout = checkoutStepRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CheckoutModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        (viewModel as CheckoutViewModel).getBillingFormVM(model)

        initView()
    }

    private fun initView() {

        with(viewModel as CheckoutViewModel)
        {
            getBillingAddressLD.observe(viewLifecycleOwner, Observer { getBilling ->

                MyApplication.getBillingResponse = getBilling
                goToBillingAddressPage()

            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })
        }

        BaseCheckoutNavigationFragment.backNavigation = false

        checkoutBottomNav.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_address -> {

                    if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.ADDRESS_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        goToBillingAddressPage()
                }
                R.id.menu_shipping -> {

                    if(MyApplication.checkoutSaveResponse?.data?.shippingMethodModel == null)
                    {
                        toast(getString(R.string.please_complete_previous_step))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.SHIPPING_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        replaceFragment(ShippingMethodFragment())


                }
                R.id.menu_payment -> {

                    if(MyApplication.checkoutSaveResponse?.data?.paymentMethodModel == null)
                    {
                        toast(getString(R.string.please_complete_previous_step))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.PAYMENT_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        replaceFragment(PaymentMethodFragment())
                }
                R.id.menu_confirm ->
                {
                    toast(getString(R.string.please_complete_previous_step))
                    return@setOnNavigationItemSelectedListener false
                }
            }
        return@setOnNavigationItemSelectedListener true

        }
    }

    private fun goToBillingAddressPage()
    {
        if(!MyApplication.getBillingResponse?.data?.disableBillingAddressCheckoutStep!!)
            replaceFragment(BillingAddressFragment())
    }

    fun updateBottomNavItem(fragment: BaseFragment) {

        val bottomNavPosition = when (fragment) {
            is ShippingMethodFragment -> CheckoutConstants.SHIPPING_TAB
            is PaymentMethodFragment -> CheckoutConstants.PAYMENT_TAB
            is PaymentInfoFragment -> CheckoutConstants.PAYMENT_TAB
            is ConfirmOrderFragment -> CheckoutConstants.CONFIRM_TAB
            else -> 0
        }

        "nop_".showLog("updateBottomNavItem $bottomNavPosition")

        checkoutBottomNav?.menu?.getItem(bottomNavPosition)?.isChecked = true
    }

    fun replaceFragment(fragment: BaseFragment) {
        updateBottomNavItem(fragment)

        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        transaction.commit()
        childFragmentManager.executePendingTransactions()
    }
    companion object{
        @JvmStatic var isBillingAddressSubmitted = false
    }

}