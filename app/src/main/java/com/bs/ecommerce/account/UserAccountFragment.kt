package com.bs.ecommerce.account

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.account.addresses.CustomerAddressFragment
import com.bs.ecommerce.account.auth.AuthModelImpl
import com.bs.ecommerce.account.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.account.auth.login.LoginFragment
import com.bs.ecommerce.account.auth.login.LoginViewModel
import com.bs.ecommerce.account.downloadableProducts.DownloadableProductListFragment
import com.bs.ecommerce.account.orders.OrderHistoryFragment
import com.bs.ecommerce.account.returnRequests.ReturnRequestHistoryFragment
import com.bs.ecommerce.account.review.CustomerReviewFragment
import com.bs.ecommerce.account.rewardpoint.RewardPointFragment
import com.bs.ecommerce.account.wishlist.WishListFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_user_account.*
import kotlinx.android.synthetic.main.item_user_account.view.*


class UserAccountFragment: ToolbarLogoBaseFragment() {

    private lateinit var logoutViewModel: LoginViewModel

    override fun getLayoutId(): Int = R.layout.fragment_user_account

    override fun getRootLayout(): RelativeLayout? = userAccountRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_ACCOUNT)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        logoutViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setLiveDataListeners()
        setupView()
    }

    private fun setLiveDataListeners() {

        (viewModel as MainViewModel).apply {

            appSettingsLD.observe(viewLifecycleOwner, Observer { settings ->

                settings.peekContent()?.let {

                    if(it.hasReturnRequests)
                        updateVisibility()

                    if (updatingAppSettings) {
                        updatingAppSettings = false

                        DbHelper.memCache = it

                        requireActivity().finish()
                        startActivity(
                            Intent(requireActivity().applicationContext, MainActivity::class.java)
                        )
                    }
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (!isShowLoader) blockingLoader.hideDialog()
            })
        }
    }

    fun updateVisibility() {
        try {
            returnRequestLayout?.visibility = View.VISIBLE
            "test~".showLog("Visibility changed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_INFO)
            setOnClickListener { clickAction(CustomerInfoFragment()) }
        }

        addressLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.app_bottom_icon_home)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_CUSTOMER_ADDRESS)
            setOnClickListener { clickAction(CustomerAddressFragment()) }
        }

        wishListLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_heart)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_WISHLIST)
            setOnClickListener { clickAction(WishListFragment(), loginRequired = false) }
        }

        orderLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_my_order)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_ORDERS)
            setOnClickListener { clickAction(OrderHistoryFragment()) }
        }

        returnRequestLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_return_request)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_RETURN_REQUESTS)
            setOnClickListener { clickAction(ReturnRequestHistoryFragment()) }
        }

        downloadableProductsLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_download)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_DOWNLOADABLE_PRODUCTS)
            setOnClickListener { clickAction(DownloadableProductListFragment()) }
        }

        cartLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_cart)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_SHOPPING_CART)
            setOnClickListener { clickAction(CartFragment()) }
        }

        rewardPointsLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_reward_points)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_REWARD_POINT)
            setOnClickListener { clickAction(RewardPointFragment()) }
        }

        reviewLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_review)
            tvOptionName.text = DbHelper.getString(Const.ACCOUNT_MY_REVIEW)
            setOnClickListener { clickAction(CustomerReviewFragment()) }
        }

        loginLayout.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_login)

            tvOptionName.text = if(prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                DbHelper.getString(Const.ACCOUNT_LOGOUT)
            else
                DbHelper.getString(Const.ACCOUNT_LOGIN)

            tvOptionName.gravity = Gravity.CENTER
            tvOptionName.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

            setOnClickListener {
                if (prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                    logoutConfirmationDialog(View.OnClickListener {
                        // updating UI
                        // setupView()

                        blockingLoader.showDialog()

                        logoutViewModel.logout(AuthModelImpl())

                        (viewModel as MainViewModel).apply {
                            updatingAppSettings = true
                            getAppSettings(MainModelImpl())
                        }
                    })
                else
                    replaceFragmentSafely(LoginFragment())
            }
        }
    }

    private fun clickAction(baseFragment: BaseFragment, loginRequired: Boolean = true) {
        if (!loginRequired || prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
            replaceFragmentSafely(baseFragment)
        else
            replaceFragmentSafely(LoginFragment())
    }
}