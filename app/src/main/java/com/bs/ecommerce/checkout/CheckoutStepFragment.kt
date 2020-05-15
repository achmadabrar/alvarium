package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class CheckoutStepFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_checkout_step

    override fun getRootLayout(): RelativeLayout = checkoutStepRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        replaceFragment(BillingAddressFragment())

        checkoutBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_address -> replaceFragment(BillingAddressFragment())
                    /*if(!isBillingAddressSubmitted)
                                        replaceFragment(BaseBillingAddressFragment())
                                     else
                                        replaceFragment(ShippingAddressFragment())*/

                R.id.menu_shipping -> replaceFragment(ShippingMethodFragment())
                R.id.menu_payment -> replaceFragment(PaymentMethodFragment())
                R.id.menu_confirm -> replaceFragment(ConfirmOrderFragment())
            }
            true
        }
    }

    fun updateBottomNavItem(fragment: BaseFragment) {

        val bottomNavPosition = when (fragment) {
            is ShippingMethodFragment -> 1
            is PaymentMethodFragment -> 2
            is ConfirmOrderFragment -> 3
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
        @JvmStatic var isShippingAddressSubmitted = false
    }

}