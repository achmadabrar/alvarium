package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartAdapter
import com.bs.ecommerce.cart.model.data.CartInfoData
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.OrderReviewAddressModel
import com.bs.ecommerce.cart.model.data.OrderReviewData
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_confirm_order.*

class ConfirmOrderFragment : BaseCheckoutNavigationFragment() {

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_confirm_order

    override fun getRootLayout(): RelativeLayout = confirmOrderRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as CheckoutViewModel).getCheckoutConfirmInformationVM(model)

        setStrings()

        setLiveDataListeners()

        checkoutButton?.setOnClickListener {
            (viewModel as CheckoutViewModel).submitConfirmOrderVM(model)
            setCurrentCartItemCounterOnTopView()
        }
    }

    private fun setStrings()
    {
        tvProductsTitle?.text = DbHelper.getString(Const.PRODUCTS)
        tvOrderCalculation?.text = DbHelper.getString(Const.ORDER_CALCULATION)
        checkoutButton?.text = DbHelper.getString(Const.CONFIRM_BUTTON)
    }



    override fun setLiveDataListeners()
    {
        super.setLiveDataListeners()

        with(viewModel as CheckoutViewModel)
        {
            getConfirmOrderLD.observe(viewLifecycleOwner, Observer { getOrderData ->

                confirmOrderRootLayout?.visibility = View.VISIBLE

                showProductList(getOrderData.data.cart)

                showOtherViews(getOrderData.data.cart.orderReviewData)

                populateOrderTable(getOrderData.data.orderTotals)


                if(getOrderData.data.selectedCheckoutAttributes.isNotEmpty())
                {
                    selectedAttributesCard.visibility = View.VISIBLE
                    selectedAttributesCard.tvCardTitle.text = DbHelper.getString(Const.SELECTED_ATTRIBUTES)
                    selectedAttributesCard.tvCardDetails.visibility = View.GONE
                    selectedAttributesCard.tvCardDetails2.text = getOrderData.data.selectedCheckoutAttributes
                    selectedAttributesCard.ivCardThumb.visibility = View.GONE
                }

            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })
        }


    }

    private fun showProductList(cartData: CartInfoData?) {

        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter = CartAdapter(cartData?.items as MutableList<CartProduct>, null, isCheckout = true,
                    showSku = cartData.showSku, isEditable = cartData.isEditable)
        }
    }

    private fun getDetailAddress(address: OrderReviewAddressModel) : String
    {
        with(address)
        {
            var address2show = ""
            address2?.let {  address2show  = "${it}\n" }

            var cityShow = ""
            city?.let {  cityShow  = "${it}\n" }

            var countryShow = ""
            countryName?.let {  countryShow  = "${it}\n" }

            return "${DbHelper.getString(Const.EMAIL)}: $email\n${DbHelper.getString(Const.PHONE)}: $phoneNumber\n$address1\n$address2show$cityShow$countryShow"
        }
    }

    private fun showOtherViews(orderReviewData: OrderReviewData) {
        allLayoutExceptButton?.visibility = View.VISIBLE
        checkoutButton?.visibility = View.VISIBLE

        if(orderReviewData.display)
        {
            with(orderReviewData.billingAddress)
            {
                billingAddressCard.tvCardTitle.text = DbHelper.getString(Const.BILLING_ADDRESS_TAB)
                billingAddressCard.tvCardDetails.text = firstName?.plus(" ")?.plus(lastName)
                billingAddressCard.tvCardDetails2.text = getDetailAddress(this)
                billingAddressCard.ivCardThumb.visibility = View.GONE
            }

            if(orderReviewData.isShippable)
            {
                with(orderReviewData.shippingAddress)
                {
                    shippingAddressCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_ADDRESS_TAB)
                    shippingAddressCard.tvCardDetails.text = firstName?.plus(" ")?.plus(lastName)
                    shippingAddressCard.tvCardDetails2.text = getDetailAddress(this)
                    shippingAddressCard.ivCardThumb.visibility = View.GONE
                }
            }
            else
                shippingAddressCard.visibility = View.GONE


            if(orderReviewData.selectedPickupInStore)
            {
                with(orderReviewData.shippingAddress)
                {
                    pickupStoreCard.tvCardTitle.text = DbHelper.getString(Const.PICK_UP_POINT_ADDRESS)
                    pickupStoreCard.tvCardDetails.text = firstName?.plus(" ")?.plus(lastName)

                    pickupStoreCard.tvCardDetails2.text = getDetailAddress(this)
                    pickupStoreCard.ivCardThumb.visibility = View.GONE
                }
            }
            else
                pickupStoreCard.visibility = View.GONE

            shippingMethodCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_METHOD)
            shippingMethodCard.tvCardDetails.visibility = View.GONE
            shippingMethodCard.tvCardDetails2.text = orderReviewData.shippingMethod ?: ""
            shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

            paymentMethodCard.tvCardTitle.text = DbHelper.getString(Const.PAYMENT_METHOD)
            paymentMethodCard.tvCardDetails.visibility = View.GONE
            paymentMethodCard.tvCardDetails2.text = orderReviewData.paymentMethod ?: ""
            paymentMethodCard.ivCardThumb.visibility = View.VISIBLE
        }
    }
}