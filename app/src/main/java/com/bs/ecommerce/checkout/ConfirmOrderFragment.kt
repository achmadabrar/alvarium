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
import com.bs.ecommerce.cart.model.data.CartInfoData
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.OrderReviewData
import com.bs.ecommerce.cart.model.data.OrderTotal
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

        setLiveDataListeners()

        checkoutButton?.setOnClickListener {   (viewModel as CheckoutViewModel).submitConfirmOrderVM(model) }
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
                selectedAttributesCard.tvCardTitle.text = "Selected Attributes"
                selectedAttributesCard.tvCardDetails.visibility = View.GONE
                selectedAttributesCard.tvCardDetails2.text = getOrderData.data.selectedCheckoutAttributes
                selectedAttributesCard.ivCardThumb.visibility = View.GONE
            }

        })

        (viewModel as CheckoutViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

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

    private fun showOtherViews(orderReviewData: OrderReviewData) {
        allLayoutExceptButton?.visibility = View.VISIBLE
        checkoutButton?.visibility = View.VISIBLE

        if(orderReviewData.display)
        {
            with(orderReviewData.billingAddress)
            {
                billingAddressCard.tvCardTitle.text = getString(R.string.billing_address)
                billingAddressCard.tvCardDetails.text = "$firstName $lastName"

                billingAddressCard.tvCardDetails2.text =
                    "Email: $email\nPhone: $phoneNumber \n$address1 \n$address2 \n$city\n$countryName"
                billingAddressCard.ivCardThumb.visibility = View.GONE
            }

            if(orderReviewData.isShippable)
            {
                with(orderReviewData.shippingAddress)
                {
                    shippingAddressCard.tvCardTitle.text = getString(R.string.shipping_address)
                    shippingAddressCard.tvCardDetails.text = "$firstName $lastName"

                    shippingAddressCard.tvCardDetails2.text =
                        "Email: $email\nPhone: $phoneNumber \n$address1 \n$address2 \n$city\n$countryName"
                    shippingAddressCard.ivCardThumb.visibility = View.GONE
                }
            }
            else
                shippingAddressCard.visibility = View.GONE


            if(orderReviewData.selectedPickupInStore)
            {
                with(orderReviewData.shippingAddress)
                {
                    pickupStoreCard.tvCardTitle.text = getString(R.string.store_pick_up)
                    pickupStoreCard.tvCardDetails.text = "$firstName $lastName"

                    pickupStoreCard.tvCardDetails2.text =
                        "Email: $email\nPhone: $phoneNumber \n$address1 \n$address2 \n$city\n$countryName"
                    pickupStoreCard.ivCardThumb.visibility = View.GONE
                }
            }
            else
                pickupStoreCard.visibility = View.GONE

            shippingMethodCard.tvCardTitle.text = "Shipping Method"
            shippingMethodCard.tvCardDetails.visibility = View.GONE
            shippingMethodCard.tvCardDetails2.text = orderReviewData.shippingMethod ?: "No Shipping Method"
            shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

            paymentMethodCard.tvCardTitle.text = "Payment Method"
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
                    taxRates?.get(0)?.rate?.let { taxKey?.text = "${getString(R.string.tax)} $it%" }

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
                tvPoints?.text = "$willEarnRewardPoints Points"
                underDiscountDivider?.visibility = View.VISIBLE
            }
            else
                pointsLayout?.visibility = View.GONE

        }
    }
}