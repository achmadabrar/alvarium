package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartAdapter
import com.bs.ecommerce.cart.CartViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.CartProduct
import com.bs.ecommerce.cart.model.PictureModel
import com.bs.ecommerce.cart.model.data.CartData
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_confirm_order.*

class ConfirmOrderFragment : BaseFragment() {
    private lateinit var model: CartModel

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_confirm_order

    override fun getRootLayout(): RelativeLayout = confirmOrderRootLayout

    override fun createViewModel(): BaseViewModel = CartViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = CartModelImpl(requireContext().applicationContext)
        viewModel  = ViewModelProvider(this).get(CartViewModel::class.java)

        (viewModel as CartViewModel).getCartData(model)

        initView()
        setLiveDataListeners()
    }

    private fun initView() {
        allLayoutExceptButton.visibility = View.VISIBLE
        checkoutButton.visibility = View.VISIBLE

        shippingAddressCard.tvCardTitle.text = getString(R.string.shipping_address)
        shippingAddressCard.tvCardDetails.text =
            "Name: John Doe\nEmail: x@y.com\nAddress: Khilgaon, Tilpapata\nCity: Dhaka\nCountry: Bangladesh"
        shippingAddressCard.ivCardThumb.visibility = View.GONE

        billingAddressCard.tvCardTitle.text = getString(R.string.billing_address)
        billingAddressCard.tvCardDetails.text =
            "Name: John Doe\nEmail: x@y.com\nAddress: Khilgaon, Tilpapata\nCity: Dhaka\nCountry: Bangladesh"
        billingAddressCard.ivCardThumb.visibility = View.GONE

        shippingMethodCard.tvCardTitle.text = "Shipping Method"
        shippingMethodCard.tvCardDetails.text = "Grounded\nNot yet shippied"
        shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

        paymentMethodCard.tvCardTitle.text = "Payment Method"
        paymentMethodCard.tvCardDetails.text = "VISA\nPending"
        paymentMethodCard.ivCardThumb.visibility = View.VISIBLE
    }

    private fun setLiveDataListeners() {

        (viewModel as CartViewModel).cartLD.observe(requireActivity(), Observer { cartData ->

            if (cartData.items.isNotEmpty()) {
                confirmOrderRootLayout?.visibility = View.VISIBLE

                activity?.let { (it as BaseActivity).updateHotCount(MyApplication.myCartCounter) }

                setData(cartData)
            }
        })

        (viewModel as CartViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }

    private fun setData(cartData: CartData?) {
        val cartAdapter = CartAdapter(cartData?.items ?: mutableListOf(), this, viewModel, model, isCheckout = true)

        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }
}