package com.bs.ecommerce.cart

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.cart.model.data.OrderTotal
import com.bs.ecommerce.checkout.CheckoutStepFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.model.data.CheckoutAttribute
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.home_fragment_bottomsheet.*
import kotlinx.android.synthetic.main.ll_cart_coupon.*
import kotlinx.android.synthetic.main.ll_cart_gift_card.*
import kotlinx.android.synthetic.main.ll_cart_title.*
import kotlinx.android.synthetic.main.table_order_total.*


class CartFragment : BaseFragment() {
    private val logTag: String = "nop_" + this::class.java.simpleName

    private var customAttributeManager: CustomAttributeManager? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>
    private lateinit var clickListener : ItemClickListener<CartProduct>

    private lateinit var model: CartModel

    override fun getFragmentTitle() = R.string.title_shopping_cart //DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_cart

    override fun getRootLayout(): RelativeLayout? = cartRootLayout

    override fun createViewModel(): BaseViewModel = CartViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            model = CartModelImpl()

            viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

            (viewModel as CartViewModel).getCartVM(model)

            initView()
        }

        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {

        with(viewModel as CartViewModel)
        {
            cartLD.observe(viewLifecycleOwner, Observer { cartRootData ->

                if(cartRootData.cart.items.isNotEmpty())
                {
                    cartRootLayout?.visibility = View.VISIBLE

                    tvTotalItem?.text = "${updateCartItemCounter(cartRootData.cart.items)} ${DbHelper.getString(Const.ITEMS)}"

                    setData(cartRootData)
                }
                else
                {
                    toast(getString(R.string.cart_empty))
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
            })
            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })
        }


    }


    private fun setData(cartRootData: CartRootData)
    {
        cartInfoLinearLayout?.visibility = View.VISIBLE

        populateProductList(cartRootData.cart.items)

        populateDiscountAndGiftCard(cartRootData)

        populateOrderTotal(cartRootData.orderTotals)

        populateDynamicAttributes(cartRootData.cart.checkoutAttributes)

        cartPageView?.visibility = View.VISIBLE
        btnCheckOut?.visibility = View.VISIBLE
    }

    private fun populateDynamicAttributes(checkoutAttributes: List<CheckoutAttribute>) {

        // setup product attributes
        customAttributeManager =
            CustomAttributeManager(
                attributes = checkoutAttributes,
                attributeViewHolder = dynamicAttributeHolderCart,
                attributeValueHolder = bottomSheetLayoutCart.attributeValueHolder,
                bsBehavior = bsBehavior
            )

        customAttributeManager?.attachAttributesToFragment()
    }

    private fun populateProductList(items: List<CartProduct>)
    {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cartproductRecyclerList?.setHasFixedSize(true)
        cartproductRecyclerList?.layoutManager = layoutManager

        val cartAdapter = CartAdapter(items, clickListener, viewModel, model)

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
                    appliedDiscountLayout?.visibility = View.VISIBLE

                    val appliedCode = appliedDiscountsWithCodes[0].couponCode

                    discountKey?.text = "${getString(R.string.discount)} ($appliedCode)"
                    appliedDiscountText?.text = "${getString(R.string.entered_code)} $appliedCode"

                    removeDiscountButton?.setOnClickListener {
                        (viewModel as CartViewModel).removeCouponVM(appliedDiscountsWithCodes[0].id, appliedCode, model)
                    }
                }
                else
                    appliedDiscountLayout?.visibility = View.GONE

                if(messages.isNotEmpty())
                    toast(messages[0])
            }
            else
                ll_cart_coupon?.visibility = View.GONE


        }

        with(cartRootData.cart.giftCardBox)
        {
            if (display)
            {
                ll_cart_gift_card?.visibility = View.VISIBLE

                if(cartRootData.orderTotals.giftCards?.isNotEmpty()!!)
                {
                    appliedGiftCardLayout?.visibility = View.VISIBLE

                    val appliedCode = cartRootData.orderTotals.giftCards!![0].couponCode

                    appliedGiftCardCodeText?.text = "${getString(R.string.entered_code)} $appliedCode"

                    removeGiftCardButton?.setOnClickListener {
                        (viewModel as CartViewModel).removeGiftCardVM(cartRootData.orderTotals.giftCards!![0].id!!.toInt(), appliedCode!!, model)
                    }
                }
                else
                    appliedGiftCardLayout?.visibility = View.GONE

            }
            else
                ll_cart_gift_card?.visibility = View.GONE

            if(message != null && message?.isNotEmpty()!!)
                toast(message.toString())
        }

    }

    private fun setLanguageStrings()
    {
        productsTitle?.text = DbHelper.getString(Const.PRODUCTS)

        etCartCoupon?.hint = DbHelper.getString(Const.ENTER_YOUR_COUPON)
        btnApplyCoupon?.text = DbHelper.getString(Const.APPLY_COUPON)

        etGiftCode?.hint = DbHelper.getString(Const.ENTER_GIFT_CARD)
        btnAddGiftCode?.text = DbHelper.getString(Const.ADD_GIFT_CARD)

        subTotalKey?.text = DbHelper.getString(Const.SUB_TOTAL)
        shippingKey?.text = DbHelper.getString(Const.SHIPPING)
        taxKey?.text = DbHelper.getString(Const.TAX)
        discountKey?.text = DbHelper.getString(Const.DISCOUNT)
        totalKey?.text = DbHelper.getString(Const.TOTAL)

        btnCheckOut?.text = DbHelper.getString(Const.CHECKOUT)

    }

    private fun initView()
    {
        setLanguageStrings()

        focusStealerCart?.requestFocus()

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutCart)
        bsBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    logTag.showLog("bs collapsed")

                    (viewModel as CartViewModel).calculateCostWithUpdatedAttributes(
                        customAttributeManager
                            ?.getFormData(Api.checkOutAttributePrefix) ?: KeyValueFormData(),
                        model
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        btnCheckOut.setOnClickListener {

            if (prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN)) {
                replaceFragmentSafely(CheckoutStepFragment())
            } else {
                showCheckOutOptionsDialogFragment()
            }
        }

        tvDone.setOnClickListener{
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //dynamicAttributeView?.onBottomSheetClose()
        }

        btnApplyCoupon?.setOnClickListener {

            val couponCode = etCartCoupon.text.toString().trim()

            if(couponCode.isNotEmpty())
            {
                (viewModel as CartViewModel).applyCouponVM(couponCode, model)
                requireActivity().hideKeyboard()
            }
        }

        btnAddGiftCode?.setOnClickListener {

            val couponCode = etGiftCode.text.toString().trim()

            if(couponCode.isNotEmpty())
            {
                (viewModel as CartViewModel).applyGiftCardVM(couponCode, model)
                requireActivity().hideKeyboard()
            }

        }

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


            if (subTotalDiscount != null)
            {
                discountLayout?.visibility = View.VISIBLE
                tvDiscount?.text = subTotalDiscount
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


        }
    }

}
