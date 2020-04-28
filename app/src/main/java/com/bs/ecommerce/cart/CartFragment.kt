package com.bs.ecommerce.cart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.cart.model.data.OrderTotal
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.ll_cart_coupon.*
import kotlinx.android.synthetic.main.ll_cart_gift_card.*
import kotlinx.android.synthetic.main.ll_cart_title.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.*
import kotlinx.android.synthetic.main.table_order_total.*


class CartFragment : BaseFragment() {
    private val logTag: String = "nop_" + this::class.java.simpleName

    private var dynamicAttributeView: CartDynamicAttribute? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    private lateinit var model: CartModel

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_cart

    override fun getRootLayout(): RelativeLayout? = cartRootLayout

    override fun createViewModel(): BaseViewModel = CartViewModel()
    


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CartModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CartViewModel::class.java)

        (viewModel as CartViewModel).getCartVM(model)


        setLiveDataListeners()

        initView()


        btnApplyCoupon?.setOnClickListener {

            val couponCode = etCartCoupon.text.toString().trim()

            if(couponCode.isNotEmpty())
                (viewModel as CartViewModel).applyCouponVM(AddDiscountPostData(data = couponCode), model)
        }

        btnAddGiftCode?.setOnClickListener {

            val couponCode = etGiftCode.text.toString().trim()

            if(couponCode.isNotEmpty())
                (viewModel as CartViewModel).applyGiftCardVM(AddDiscountPostData(data = couponCode), model)
        }

    }


    private fun setLiveDataListeners() {

        (viewModel as CartViewModel).cartLD.observe(viewLifecycleOwner, Observer { cartRootData ->

            if(cartRootData.cart.items.isNotEmpty())
            {
                cartRootLayout?.visibility = View.VISIBLE

                tvTotalItem?.text = getString(R.string.cart_items_count, cartRootData.cart.items.size)
                MyApplication.setCartCounter(cartRootData.cart.items.size)

                activity?.let {  (it as BaseActivity).updateHotCount(MyApplication.myCartCounter)    }


                setData(cartRootData)
            }
            else
            {

                toast(getString(R.string.cart_empty))
                requireActivity().supportFragmentManager.popBackStackImmediate()
                MyApplication.setCartCounter(0)

            }

        })

        (viewModel as CartViewModel).isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

        (viewModel as CartViewModel).selectedAttrLD.observe(
            viewLifecycleOwner,
            Observer { attrMap ->

                for (i in attrMap.keys) {
                    val view = dynamicAttributeHolderCart.findViewWithTag<View>(i)
                    val textView = view.findViewById<TextView>(R.id.tvSelectedAttr)

                    val selectedAttr = attrMap[i]

                    if (selectedAttr.isNullOrEmpty()) {
                        textView?.text = getString(R.string.select)
                    } else {
                        textView?.text = attrMap[i]?.get(0)?.name
                    }
                }
            })

    }


    private fun setData(cartRootData: CartRootData)
    {
        //MyApplication.setCartCounter(cartProductListResponse.count)
        cartInfoLinearLayout?.visibility = View.VISIBLE

        populateProductList(cartRootData.cart.items)

        populateDiscountAndGiftCard(cartRootData)

        populateOrderTotal(cartRootData.orderTotals)

        populateDynamicAttributes()

        cartPageView?.visibility = View.VISIBLE
        btnCheckOut?.visibility = View.VISIBLE
    }

    private fun populateDynamicAttributes() {
        dynamicAttributeHolderCart.removeAllViews()

        bottomSheetLayoutCart?.let {

            dynamicAttributeView = CartDynamicAttribute(
                requireContext(),
                viewModel as CartViewModel,
                it,
                bsBehavior
            )

            for (i in dynamicAttributeView!!.getAttrViews()) {
                dynamicAttributeHolderCart?.addView(i)
            }
        }
    }

    private fun populateProductList(items: List<CartProduct>)
    {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cartproductRecyclerList?.setHasFixedSize(true)
        cartproductRecyclerList?.layoutManager = layoutManager

        val cartAdapter = CartAdapter(items, this, viewModel, model)

        cartproductRecyclerList?.adapter = cartAdapter
    }

    private fun populateDiscountAndGiftCard(cartRootData: CartRootData)
    {

        with(cartRootData.cart.discountBox)
        {
            if (display)
            {
                ll_cart_coupon?.visibility = View.VISIBLE

                if(appliedDiscountsWithCodes.isNotEmpty())
                {
                    discountKey?.text = "${getString(R.string.discount)} (${appliedDiscountsWithCodes[0].couponCode})"
                }

                if(messages.isNotEmpty())
                    etCartCoupon?.setText(messages[0])
            }
            else
                ll_cart_coupon?.visibility = View.GONE

            if(messages.isNotEmpty())
                toast(messages[0])
        }

        with(cartRootData.cart.giftCardBox)
        {
            if (display)
            {
                ll_cart_gift_card?.visibility = View.VISIBLE

/*                if(message.isNotEmpty())
                    etGiftCode?.setText(message)*/
            }
            else
                ll_cart_gift_card?.visibility = View.GONE

            if(message != null)
                toast(message.toString())
        }

    }

    private fun initView()
    {
        focusStealerCart?.requestFocus()

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutCart)
        bsBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    logTag.showLog("bs collapsed")

                    (viewModel as CartViewModel).calculateCostWithUpdatedAttributes(model)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        btnCheckOut.setOnClickListener{
            showCheckOutOptionsDialogFragment()
        }

        tvDone.setOnClickListener{
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            dynamicAttributeView?.onBottomSheetClose()
        }
    }

    private fun showCheckOutOptionsDialogFragment() {
        val newFragment = GuestCheckoutFragment()
        newFragment.show(requireActivity().supportFragmentManager, "dialog")
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

            if (orderTotal.isEmpty()) {
                tvTotal?.setText(R.string.calculated_during_checkout)
                tvTotal?.setTextColor(Color.RED)
            }
            if (shipping.isEmpty()) {
                tvTotal?.setText(R.string.calculated_during_checkout)
                tvTotal?.setTextColor(Color.RED)
            }
        }
    }

}
