package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class CheckoutStepFragment : ToolbarLogoBaseFragment() {

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_checkout_step

    override fun getRootLayout(): RelativeLayout = checkoutStepRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutAddressViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel  = ViewModelProvider(this).get(CheckoutAddressViewModel::class.java)
    }

    private fun initView() {

        BaseCheckoutNavigationFragment.backNavigation = false

        replaceFragment(BillingAddressFragment())

        checkoutBottomNav.setOnNavigationItemSelectedListener { item ->

            if (requireActivity().supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is PaymentInfoFragment)
            {
                toast(getString(R.string.skip_payment_dialog))  //TODO will be a dialog
                false
            }
            else
            {
                when (item.itemId) {
                    R.id.menu_address -> replaceFragment(BillingAddressFragment())
                    R.id.menu_shipping -> replaceFragment(ShippingMethodFragment())
                    R.id.menu_payment -> replaceFragment(PaymentMethodFragment())
                    R.id.menu_confirm -> replaceFragment(ConfirmOrderFragment())
                }
                true
            }

        }
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