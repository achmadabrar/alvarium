package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartAdapter
import com.bs.ecommerce.cart.GiftCardAdapter
import com.bs.ecommerce.cart.model.data.*
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showTextPendingCalculationOnCheckout
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_confirm_order.*
import kotlinx.android.synthetic.main.table_order_total.*

class ConfirmOrderFragment : BaseCheckoutNavigationFragment() {

    private lateinit var clickListener : ItemClickListener<CartProduct>

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_confirm_order

    override fun getRootLayout(): RelativeLayout = confirmOrderRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as CheckoutViewModel).getCheckoutConfirmInformationVM(model)

        clickListener = object : ItemClickListener<CartProduct> {

            override fun onClick(view: View, position: Int, data: CartProduct) {

                when (view.id) {

                    R.id.itemView ->
                        data.productId?.let {
                            replaceFragmentSafely(ProductDetailFragment.newInstance(it, data.productName))
                        }
                }
            }
        }

        setStrings()

        setLiveDataListeners()

        checkoutButton?.setOnClickListener {   (viewModel as CheckoutViewModel).submitConfirmOrderVM(model) }
    }


    private fun setStrings()
    {
        tvProductsTitle?.text = DbHelper.getString(Const.PRODUCTS)
        tvOrderCalculation?.text = DbHelper.getString(Const.ORDER_CALCULATION)
        pointsKey?.text = DbHelper.getString(Const.WILL_EARN)
        checkoutButton?.text = DbHelper.getString(Const.CONFIRM_BUTTON)
    }



    override fun setLiveDataListeners()
    {
        super.setLiveDataListeners()

        (viewModel as CheckoutViewModel).getConfirmOrderLD.observe(requireActivity(), Observer { getOrderData ->

            confirmOrderRootLayout?.visibility = View.VISIBLE

            showProductList(getOrderData.data.cart)

            showOtherViews(getOrderData.data.cart.orderReviewData)

            populateOrderTotal(getOrderData.data.orderTotals)


            if(getOrderData.data.selectedCheckoutAttributes.isNotEmpty())
            {
                selectedAttributesCard.visibility = View.VISIBLE
                selectedAttributesCard.tvCardTitle.text = DbHelper.getString(Const.SELECTED_ATTRIBUTES)
                selectedAttributesCard.tvCardDetails.visibility = View.GONE
                selectedAttributesCard.tvCardDetails2.text = getOrderData.data.selectedCheckoutAttributes
                selectedAttributesCard.ivCardThumb.visibility = View.GONE
            }

        })

        (viewModel as CheckoutViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader -> showHideLoader(isShowLoader) })

    }

    private fun showProductList(cartData: CartInfoData?) {
        val cartAdapter = CartAdapter(cartData?.items ?: mutableListOf(),clickListener,   viewModel, isCheckout = true)

        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    private fun getDetailAddress(address: OrderReviewAddressModel) : String
    {
        with(address)
        {
            return "${DbHelper.getString(Const.EMAIL)}: $email\n${DbHelper.getString(Const.PHONE)}: $phoneNumber \n$address1 \n$address2 \n$city\n$countryName"
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
                billingAddressCard.tvCardDetails.text = "$firstName $lastName"
                billingAddressCard.tvCardDetails2.text = getDetailAddress(this)
                billingAddressCard.ivCardThumb.visibility = View.GONE
            }

            if(orderReviewData.isShippable)
            {
                with(orderReviewData.shippingAddress)
                {
                    shippingAddressCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_ADDRESS_TAB)
                    shippingAddressCard.tvCardDetails.text = "$firstName $lastName"
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
                    pickupStoreCard.tvCardDetails.text = "$firstName $lastName"

                    pickupStoreCard.tvCardDetails2.text = getDetailAddress(this)
                    pickupStoreCard.ivCardThumb.visibility = View.GONE
                }
            }
            else
                pickupStoreCard.visibility = View.GONE

            shippingMethodCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_METHOD)
            shippingMethodCard.tvCardDetails.visibility = View.GONE
            shippingMethodCard.tvCardDetails2.text = orderReviewData.shippingMethod ?: "No Shipping Method"
            shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

            paymentMethodCard.tvCardTitle.text = DbHelper.getString(Const.PAYMENT_METHOD)
            paymentMethodCard.tvCardDetails.visibility = View.GONE
            paymentMethodCard.tvCardDetails2.text = orderReviewData.paymentMethod ?: "No Payment Method"
            paymentMethodCard.ivCardThumb.visibility = View.VISIBLE
        }
    }


    private fun populateOrderTotal(orderTotalModel: OrderTotal)
    {
        with(orderTotalModel)
        {
            tvSubTotal?.text = subTotal
            tvShippingCharge?.text = shipping


            if (displayTax && tax != null)
            {
                if (displayTaxRates)
                    taxRates?.get(0)?.rate?.let { taxKey?.text = "${DbHelper.getString(Const.TAX)} $it%" }

                tvTax?.text = tax
            }
            else
                taxLayout?.visibility = View.GONE

            tvTotal?.text = orderTotal


            if (orderTotalDiscount != null)
            {
                discountLayout?.visibility = View.VISIBLE
                tvDiscount?.text = orderTotalDiscount
                underDiscountDivider?.visibility = View.VISIBLE
            }
            else
                discountLayout?.visibility = View.GONE

            if (giftCards != null && giftCards!!.isNotEmpty())
            {
                giftCardLayout?.visibility = View.VISIBLE

                underGiftCardDivider?.visibility = View.VISIBLE

                val giftCardAdapter = GiftCardAdapter(activity!!, giftCards!!)
                giftCardRecyclerList?.layoutManager = LinearLayoutManager(activity)
                giftCardRecyclerList?.adapter = giftCardAdapter
            }
            else
                giftCardLayout?.visibility = View.GONE

            orderTotal?.let {

                if (it.isEmpty())
                    tvTotal?.showTextPendingCalculationOnCheckout()
            } ?: tvTotal?.showTextPendingCalculationOnCheckout()


            shipping?.let {

                if (it.isEmpty())
                    tvShippingCharge?.showTextPendingCalculationOnCheckout()
            } ?: tvShippingCharge?.showTextPendingCalculationOnCheckout()


            if (willEarnRewardPoints != null && willEarnRewardPoints != 0)
            {
                pointsLayout?.visibility = View.VISIBLE
                tvPoints?.text = DbHelper.getStringWithNumber(Const.POINTS, willEarnRewardPoints!!)
                underDiscountDivider?.visibility = View.VISIBLE
            }
            else
                pointsLayout?.visibility = View.GONE

        }
    }
}