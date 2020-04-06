package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class CheckoutStepFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_checkout_step

    override fun getRootLayout(): RelativeLayout = checkoutStepRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        replaceFragment(BaseBillingAddressFragment())

        checkoutBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_address -> replaceFragment(BaseBillingAddressFragment())
                R.id.menu_shipping -> replaceFragment(ShippingMethodFragment())
                R.id.menu_payment -> replaceFragment(PaymentMethodFragment())
                R.id.menu_confirm -> replaceFragment(ConfirmOrderFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: BaseFragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        transaction.commit()
        childFragmentManager.executePendingTransactions()
    }
}