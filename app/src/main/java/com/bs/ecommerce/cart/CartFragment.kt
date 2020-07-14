package com.bs.ecommerce.cart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.login.LoginFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.cart.model.data.EstimateShipping
import com.bs.ecommerce.catalog.common.CheckoutAttribute
import com.bs.ecommerce.catalog.product.ProductDetailFragment
import com.bs.ecommerce.checkout.CheckoutStepFragment
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.estimate_shipping_layout.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
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

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

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

                tvTotalItem?.text = DbHelper.getStringWithNumber(Const.ITEMS, updateCartItemCounter(cartRootData.cart.items))

                if(cartRootData.cart.items.isNotEmpty())
                {
                    cartRootLayout?.visibility = View.VISIBLE

                    try {
                        setData(cartRootData)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else
                {
                    toast(DbHelper.getString(Const.CART_EMPTY))
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })

            uploadFileLD.observe(viewLifecycleOwner, Observer { data ->
                blockingLoader.hideDialog()

                customAttributeManager?.updateSelectedAttr(-5, data?.downloadGuid ?: "")

                //TODO download uploaded file not done
                /*fabDownloadFileCartPage?.visibility = View.VISIBLE
                fabDownloadFileCartPage?.setOnClickListener {
                    viewModel.downloadFile(data?.downloadUrl)
                }*/
            })

            stateListLD.observe(viewLifecycleOwner, Observer { statesList ->

                EditTextUtils().populateStateSpinner(
                    requireContext(),
                    estimateShippingLayout.stateSpinner,
                    -1,
                    statesList
                )
            })

            estimateShippingLD.observe(viewLifecycleOwner, Observer { data ->

                val optionOne = if(data?.shippingOptions?.size ?: 0 > 0)
                    data.shippingOptions?.get(0) else null

                val optionTwo = if(data?.shippingOptions?.size ?: 0 > 1)
                    data.shippingOptions?.get(1) else null

                estimateShippingLayout?.tvResult?.text =
                    optionOne?.name?.plus(" (")?.plus(optionOne.price)?.plus(" )")
                        ?.plus("\n")
                        ?.plus(optionTwo?.name)?.plus(" (")?.plus(optionTwo?.price)?.plus(" )")

                estimateShippingLayout?.tvResult?.visibility = View.VISIBLE
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ATTRIBUTE_FILE_UPLOAD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            data?.data?.let {
                blockingLoader.showDialog()

                val fileWithMime = Utils().processFileForUploading(it, requireActivity())

                if(fileWithMime != null) {
                    (viewModel as CartViewModel)
                        .uploadFile(fileWithMime.file, fileWithMime.mimeType, model)
                } else {
                    blockingLoader.hideDialog()
                }
            }
        }
    }

    private fun setData(cartRootData: CartRootData)
    {
        cartInfoLinearLayout?.visibility = View.VISIBLE

        populateProductList(cartRootData.cart.items, cartRootData.cart.showSku, cartRootData.cart.isEditable)

        populateDiscountAndGiftCard(cartRootData)

        populateOrderTable(cartRootData.orderTotals)

        populateDynamicAttributes(cartRootData.cart.checkoutAttributes)

        populateEstimateShipping(cartRootData.estimateShipping)

        cartPageView?.visibility = View.VISIBLE
        btnCheckOut?.visibility = View.VISIBLE
    }

    private fun populateEstimateShipping(estimateShipping: EstimateShipping?) {

        if(estimateShipping?.enabled == false) return

        estimateShippingLayout?.apply {

            tvSectionTitle?.text = DbHelper.getString(Const.CART_ESTIMATE_SHIPPING_TITLE)
            tvSectionSubtitle?.text = DbHelper.getString(Const.CART_ESTIMATE_SHIPPING_SUBTITLE)
            btnCalculate?.text = DbHelper.getString(Const.CART_ESTIMATE_SHIPPING_BTN)
            etZipCode?.hint = DbHelper.getString(Const.CART_ESTIMATE_SHIPPING_ZIP)
            etZipCode?.setDrawableEnd(R.drawable.ic_star_formular)

            btnCalculate?.setOnClickListener {

                estimateShipping?.let {
                    it.countryId = (countrySpinner.selectedItem as AvailableCountry).value.toInt()
                    it.stateProvinceId = (stateSpinner.selectedItem as AvailableState).id
                    it.zipPostalCode = etZipCode?.text?.toString() ?: ""

                    (viewModel as CartViewModel).estimateShippingCost( it, model)
                }
            }

            EditTextUtils().populateCountrySpinner(
                requireContext(),
                countrySpinner,
                estimateShipping?.availableCountries ?: listOf(),

                object : ItemClickListener<AvailableCountry> {

                    override fun onClick(view: View, position: Int, data: AvailableCountry) {

                        // load states for selected country
                        if (data.value != "0") {
                            (viewModel as CartViewModel).getStatesByCountryVM(
                                data.value, CheckoutModelImpl()
                            )
                        }
                    }
                }
            )

            EditTextUtils().populateStateSpinner(
                requireContext(),
                stateSpinner,
                -1,
                estimateShipping?.availableStates ?: listOf()
            )
        }

    }

    private fun populateDynamicAttributes(checkoutAttributes: List<CheckoutAttribute>) {

        // setup product attributes
        customAttributeManager =
            CustomAttributeManager(
                activity = requireActivity(),
                attributes = checkoutAttributes,
                attributeViewHolder = dynamicAttributeHolderCart,
                attributeValueHolder = bottomSheetLayoutCart.attributeValueHolder,
                bsBehavior = bsBehavior
            )

        customAttributeManager?.attachAttributesToFragment()
    }

    private fun populateProductList(items: List<CartProduct>, showSku: Boolean = false, editable: Boolean = false)
    {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cartproductRecyclerList?.setHasFixedSize(true)
        cartproductRecyclerList?.layoutManager = layoutManager

//        val cartAdapter = CartAdapter(requireActivity(), items, clickListener, viewModel, model)
        val cartAdapter = CartAdapter2(items as MutableList<CartProduct>, clickListener, isCheckout = false,
            showSku = showSku, isEditable = editable)

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

                    discountKey?.text = DbHelper.getString(Const.DISCOUNT).plus(" ").plus(appliedCode)
                    appliedDiscountText?.text = DbHelper.getStringWithNumber(Const.ENTERED_COUPON_CODE, appliedCode)

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

                if(cartRootData.orderTotals.giftCards?.isNotEmpty() == true)
                {
                    appliedGiftCardLayout?.visibility = View.VISIBLE

                    val appliedCode = cartRootData.orderTotals.giftCards?.get(0)?.couponCode ?: ""

                    appliedGiftCardCodeText?.text = DbHelper.getStringWithNumber(Const.ENTERED_COUPON_CODE, appliedCode)

                    removeGiftCardButton?.setOnClickListener {
                        cartRootData.orderTotals.giftCards?.get(0)?.id?.toInt()?.let {
                            (viewModel as CartViewModel).removeGiftCardVM(it, appliedCode, model)
                        }
                    }
                }
                else
                    appliedGiftCardLayout?.visibility = View.GONE

            }
            else
                ll_cart_gift_card?.visibility = View.GONE

            message?.let { if(it.isNotEmpty()) toast(it) }
        }

    }

    private fun setLanguageStrings()
    {
        productsTitle?.text = DbHelper.getString(Const.PRODUCTS)

        etCartCoupon?.hint = DbHelper.getString(Const.ENTER_YOUR_COUPON)
        btnApplyCoupon?.text = DbHelper.getString(Const.APPLY_COUPON)

        etGiftCode?.hint = DbHelper.getString(Const.ENTER_GIFT_CARD)
        btnAddGiftCode?.text = DbHelper.getString(Const.ADD_GIFT_CARD)

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

                    submitCheckoutAttributes()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        btnCheckOut.setOnClickListener {

            submitCheckoutAttributes()

            if (prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                replaceFragmentSafely(CheckoutStepFragment())

            else if(!MyApplication.isAnonymousCheckoutAllowed)
                replaceFragmentSafely(LoginFragment())
            else
                showCheckOutOptionsDialogFragment()

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

                    R.id.icRemoveItem ->
                        (viewModel as CartViewModel).removeItemFromCart(data.id, model)

                    R.id.btnPlus ->
                        (viewModel as CartViewModel).updateQuantity(data, true, model)

                    R.id.btnMinus ->
                        (viewModel as CartViewModel).updateQuantity(data, false, model)
                }
            }
        }
    }

    private fun showCheckOutOptionsDialogFragment() {
        val newFragment = GuestCheckoutFragment()
        newFragment.show(requireActivity().supportFragmentManager, "dialog")
    }


    private fun submitCheckoutAttributes()
    {
        (viewModel as CartViewModel).calculateCostWithUpdatedAttributes(
            customAttributeManager
                ?.getFormData(Api.checkOutAttributePrefix) ?: KeyValueFormData(),
            model)
    }

}
