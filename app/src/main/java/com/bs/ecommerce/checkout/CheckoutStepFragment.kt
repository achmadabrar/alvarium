package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.Menu
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
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class CheckoutStepFragment : ToolbarLogoBaseFragment() {


    private lateinit var model: CheckoutModel

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_checkout_step

    override fun getRootLayout(): RelativeLayout = checkoutStepRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CheckoutModelImpl()

        viewModel  = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        (viewModel as CheckoutViewModel).getBillingFormVM(model)

        initView()

        setTopNavigation()
    }

    private fun setTopNavigation()
    {
        checkoutBottomNav?.apply{
            menu.clear()

            menu.add(Menu.NONE, R.id.menu_address, 0, DbHelper.getString(Const.ADDRESS_TAB)).setIcon(R.drawable.ic_address)
            menu.add(Menu.NONE, R.id.menu_shipping, 1, DbHelper.getString(Const.SHIPPING_TAB)).setIcon(R.drawable.ic_truck)
            menu.add(Menu.NONE, R.id.menu_payment, 2, DbHelper.getString(Const.PAYMENT_TAB)).setIcon(R.drawable.ic_payment)
            menu.add(Menu.NONE, R.id.menu_confirm, 3, DbHelper.getString(Const.CONFIRM_TAB)).setIcon(R.drawable.ic_check_circle)

        }
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

        checkoutBottomNav.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_address -> {

                    if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.ADDRESS_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        goToBillingAddressPage()
                }
                R.id.menu_shipping -> {

                    val prohibitedList = listOf(R.id.menu_address)

                    if(prohibitedList.contains(MyApplication.previouslySelectedTab))
                    {
                        toast(DbHelper.getString(Const.PLEASE_COMPLETE_PREVIOUS_STEP))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else if(MyApplication.checkoutSaveResponse?.data?.shippingMethodModel == null)
                    {
                        toast(DbHelper.getString(Const.PLEASE_COMPLETE_PREVIOUS_STEP))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.SHIPPING_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        replaceFragment(ShippingMethodFragment())

                }
                R.id.menu_payment -> {

                    val prohibitedList = listOf(R.id.menu_address, R.id.menu_shipping)

                    if(prohibitedList.contains(MyApplication.previouslySelectedTab))
                    {
                        toast(DbHelper.getString(Const.PLEASE_COMPLETE_PREVIOUS_STEP))
                        return@setOnNavigationItemSelectedListener false
                    }
                    if(MyApplication.checkoutSaveResponse?.data?.paymentMethodModel == null)
                    {
                        toast(DbHelper.getString(Const.PLEASE_COMPLETE_PREVIOUS_STEP))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else if(checkoutBottomNav?.menu?.getItem(CheckoutConstants.PAYMENT_TAB)?.isChecked!!)
                        return@setOnNavigationItemSelectedListener false
                    else
                        replaceFragment(PaymentMethodFragment())
                }
                R.id.menu_confirm ->
                {
                    toast(DbHelper.getString(Const.PLEASE_COMPLETE_PREVIOUS_STEP))
                    return@setOnNavigationItemSelectedListener false
                }
            }
            MyApplication.previouslySelectedTab = item.itemId
            return@setOnNavigationItemSelectedListener true

        }
    }

    private fun goToBillingAddressPage()
    {
        // if(!MyApplication.getBillingResponse?.data?.disableBillingAddressCheckoutStep!!)
            replaceFragment(BillingAddressFragment())
    }

    fun updateBottomNavItem(fragment: BaseFragment) {

        val bottomNavPosition = when (fragment) {
            is ShippingMethodFragment -> CheckoutConstants.SHIPPING_TAB
            is PaymentMethodFragment -> CheckoutConstants.PAYMENT_TAB
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