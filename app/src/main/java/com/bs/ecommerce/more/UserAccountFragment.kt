package com.bs.ecommerce.more

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.auth.login.LoginFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_user_account.*
import kotlinx.android.synthetic.main.item_user_account.view.*


class UserAccountFragment: BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_user_account

    override fun getRootLayout(): RelativeLayout? = userAccountRootLayout

    override fun createViewModel(): BaseViewModel = BaseViewModel()

    override fun getFragmentTitle(): Int = R.string.my_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {

        prefObject.getCustomerInfo(PrefSingleton.CUSTOMER_INFO)?.apply {
            tvUserName.text = firstName.plus(" ").plus(lastName)
            tvUserEmail.text = email
        } ?: apply {
            tvUserName.visibility = View.GONE
            tvUserEmail.visibility = View.GONE
        }

        accInfoLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.app_bottom_icon_account)
            tvOptionName.text = getString(R.string.account_info)
            setOnClickListener { clickAction(CustomerInfoFragment()) }
        }

        addressLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.app_bottom_icon_home)
            tvOptionName.text = getString(R.string.my_addresses)
            setOnClickListener { clickAction(CustomerAddressFragment()) }
        }

        wishListLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_heart)
            tvOptionName.text = getString(R.string.title_wishlist)
            setOnClickListener { clickAction(WishListFragment()) }
        }

        orderLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_my_order)
            tvOptionName.text = getString(R.string.my_orders)
            setOnClickListener { clickAction(OrderHistoryFragment()) }
        }

        cartLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.app_icon_cart)
            tvOptionName.text = getString(R.string.shopping_cart)
            setOnClickListener { clickAction(CartFragment()) }
        }

        reviewLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.app_icon_cart)
            tvOptionName.text = getString(R.string.my_reviews)
            setOnClickListener { toast("No Implemented") }
        }

        loginLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_login)

            tvOptionName.text = if(prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                getString(R.string.log_out)
            else
                getString(R.string.login)

            tvOptionName.gravity = Gravity.CENTER
            tvOptionName.setTextColor(resources.getColor(R.color.red))

            setOnClickListener {
                if (prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                    logoutConfirmationDialog(View.OnClickListener {
                        // updating UI
                        setupView()
                    })
                else
                    replaceFragmentSafely(LoginFragment())
            }
        }
    }

    private fun clickAction(baseFragment: BaseFragment) {
        if (prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
            replaceFragmentSafely(baseFragment)
        else
            replaceFragmentSafely(LoginFragment())
    }
}