package com.bs.ecommerce.product

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.more.ProductReviewFragment
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.adapter.AssociatedProductAdapter
import com.bs.ecommerce.product.adapter.DetailsSliderAdapter
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.viewModel.ProductDetailViewModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.featured_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.fragment_product_detail.view.*
import kotlinx.android.synthetic.main.product_availability_layout.view.*
import kotlinx.android.synthetic.main.product_gift_card_layout.*
import kotlinx.android.synthetic.main.product_gift_card_layout.view.*
import kotlinx.android.synthetic.main.product_manufacturer_layout.view.*
import kotlinx.android.synthetic.main.product_manufacturer_layout.view.tvSectionTitle
import kotlinx.android.synthetic.main.product_name_layout.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.tvProductName
import kotlinx.android.synthetic.main.product_price_layout.view.*
import kotlinx.android.synthetic.main.product_quantity.view.*
import kotlinx.android.synthetic.main.product_rating_layout.view.*
import kotlinx.android.synthetic.main.product_vendor_layout.view.*
import kotlinx.android.synthetic.main.rental_product_layout.view.*
import kotlinx.android.synthetic.main.slider.view.*
import java.util.*


class ProductDetailFragment : BaseFragment(), View.OnClickListener {

    private lateinit var bsBehavior: BottomSheetBehavior<*>
    private lateinit var model: ProductDetailModel
    private lateinit var listItemClickListener: ItemClickListener<ProductSummary>
    private var customAttributeManager: CustomAttributeManager? = null

    override fun getFragmentTitle() = arguments?.getString(PRODUCT_NAME, "") ?: ""

    override fun getLayoutId(): Int = R.layout.fragment_product_detail

    override fun getRootLayout(): RelativeLayout = productDetailsRootLayout

    override fun createViewModel(): BaseViewModel =
        ProductDetailViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments?.getLong(PRODUCT_ID) == null) {
            toast(DbHelper.getString(Const.INVALID_PRODUCT))
            return
        }

        if(!viewCreated) {

            val productId: Long = arguments?.getLong(PRODUCT_ID) ?: -1

            initView(productId)

            model = ProductDetailModelImpl()
            viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

            (viewModel as ProductDetailViewModel).apply {

                if(arguments?.getBoolean(ASSOCIATED_PRODUCT) == true) {

                    setAssociatedProduct(associatedProduct)
                    associatedProduct = null

                } else {

                    getProductDetail(productId, model)

                    getRelatedProducts(
                        productId, resources.getDimensionPixelSize(R.dimen.product_item_size), model
                    )

                    getSimilarProducts(
                        productId, resources.getDimensionPixelSize(R.dimen.product_item_size), model
                    )
                }
            }
        }

        try { setLiveDataListeners() } catch (e: Exception) {e.printStackTrace() }
    }

    override fun onResume() {
        super.onResume()

        arguments?.getString(PRODUCT_NAME)?.let {
            activity?.title = it
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setLiveDataListeners() {

        (viewModel as ProductDetailViewModel).apply {

            productLiveData.observe(
                viewLifecycleOwner,
                Observer { product ->
                    // Set Toolbar title
                    if(isAdded && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        requireActivity().title = product?.name ?: ""
                        arguments?.putString(PRODUCT_NAME, product?.name ?: "")
                    }

                    product?.let {

                        // slider image
                        val imageSlider = vsImageSlider?.inflate()

                        val sliderData = if (product.pictureModels.isNullOrEmpty())
                            listOf(product.defaultPictureModel)
                        else
                            product.pictureModels

                        val detailsSliderAdapter = DetailsSliderAdapter(requireContext(), sliderData)
                        imageSlider?.view_pager_slider?.adapter = detailsSliderAdapter
                        imageSlider?.view_pager_slider?.currentItem = 0
                        imageSlider?.circle_indicator?.setViewPager(imageSlider.view_pager_slider)

                        imageSlider?.circle_indicator?.pageColor =
                            ContextCompat.getColor(requireContext(), R.color.white)
                        imageSlider?.circle_indicator?.fillColor =
                            ContextCompat.getColor(requireContext(), R.color.darkOrGray)

                        detailsSliderAdapter.setOnSliderClickListener(object :
                            DetailsSliderAdapter.OnSliderClickListener {
                            override fun onSliderClick(view: View, position: Int) {

                                // Open fullscreen image activity
                                FullScreenImageActivity.sliderPosition = position
                                FullScreenImageActivity.pictureModels = sliderData
                                val intent = Intent(activity, FullScreenImageActivity::class.java)
                                startActivity(intent)
                            }
                        })

                        // short description
                        val productNameLayout = vsProductNameLayout?.inflate()
                        productNameLayout?.tvProductName?.text = product.name
                        product.shortDescription?.let {
                            productNameLayout?.tvProductDescription?.show(it)
                        }

                        // product rating & review section
                        productRatingLayout.ratingBar.rating =
                            (product.productReviewOverview?.ratingSum ?: 0).toFloat()

                        productRatingLayout.tvReviewCount.text = (product.productReviewOverview?.totalReviews ?: 0)
                            .toString().plus(" ")
                            .plus(DbHelper.getString(Const.TITLE_REVIEW))

                        productRatingLayout.setOnClickListener {

                            if(product.productReviewOverview?.allowCustomerReviews == true) {
                                product.id?.let {
                                    replaceFragmentSafely(ProductReviewFragment.newInstance(it))
                                }
                            }
                        }


                        // product price
                        productPriceLayout.tvOriginalPrice.apply {
                            if (product.productPrice?.oldPrice == null) {
                                visibility = View.GONE
                            } else {
                                text = product.productPrice.oldPrice
                                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                            }
                        }

                        if (product.addToCart?.customerEntersPrice == true) {

                            productPriceLayout?.tvDiscountPrice?.visibility = View.GONE
                            productPriceLayout?.priceInputLl?.visibility = View.VISIBLE

                            productPriceLayout?.etPrice?.hint = product.addToCart.customerEnteredPriceRange
                        }

                        //productPriceLayout.tvDiscountPercent.text = "40% Off"

                        // PRODUCT MANUFACTURER LAYOUT
                        product.productManufacturers?.let {
                            if(it.isNotEmpty()) {
                                hd16.visibility = View.VISIBLE
                                productManufacturer?.visibility = View.VISIBLE

                                productManufacturer?.ll?.removeAllViews()
                                productManufacturer?.tvSectionTitle?.text =
                                    DbHelper.getString(Const.PRODUCT_MANUFACTURER)

                                for(temp in it) {
                                    val tv = TextView(requireContext(), null, 0, R.style.productPageDynamicText)
                                    tv.text = temp.name
                                    tv.setOnClickListener {
                                        replaceFragmentSafely(ProductListFragment.newInstance(
                                            temp.name ?: "", temp.id ?: -1, ProductListFragment.GetBy.MANUFACTURER
                                        ))
                                    }

                                    productManufacturer?.ll?.addView(tv)
                                }
                            }
                        }

                        // PRODUCT TAG LAYOUT
                        product.productTags?.let {
                            if(it.isNotEmpty()) {
                                hd17.visibility = View.VISIBLE
                                productTag?.visibility = View.VISIBLE

                                productTag?.ll?.removeAllViews()
                                productTag?.tvSectionTitle?.text =
                                    DbHelper.getString(Const.PRODUCT_TAG)

                                for(temp in it) {
                                    val tv = TextView(requireContext(), null, 0, R.style.productPageDynamicText)
                                    tv.text = temp.name
                                    tv.setOnClickListener {
                                        replaceFragmentSafely(
                                            ProductListFragment.newInstance(temp.name ?: "", temp.id ?: -1, ProductListFragment.GetBy.TAG)
                                        )
                                    }

                                    productTag?.ll?.addView(tv)
                                }
                            }
                        }

                        // PRODUCT VENDOR LAYOUT
                        if(product.vendorModel != null && product.showVendor == true) {
                            hd18.visibility = View.VISIBLE
                            productVendor1?.visibility = View.VISIBLE

                            productVendor1?.vendorLl?.removeAllViews()
                            productVendor1?.tvSectionTitle?.text =
                                DbHelper.getString(Const.PRODUCT_VENDOR)

                            val tv = TextView(requireContext(), null, 0, R.style.productPageDynamicText)
                            tv.text = product.vendorModel.name
                            tv.setOnClickListener {
                                replaceFragmentSafely(
                                    ProductListFragment.newInstance(product.vendorModel.name ?: "", product.vendorModel.id ?: -1, ProductListFragment.GetBy.VENDOR)
                                )
                            }

                            productVendor1?.vendorLl?.addView(tv)
                        }

                        availabilityLayout.tvAvailabilityTitle.text = DbHelper.getString(Const.PRODUCT_AVAILABILITY)
                        availabilityLayout.tvAvailability.text = product.stockAvailability
                        availabilityLayout.deliveryMethod.text = DbHelper.getString(Const.PRODUCT_FREE_SHIPPING)
                        availabilityLayout.deliveryMethod.visibility = if (product.isFreeShipping == true)
                            View.VISIBLE else View.GONE

                        productQuantityLayout.tvQuantityTitle.text = DbHelper.getString(Const.PRODUCT_QUANTITY)
                        productQuantityLayout.tvQuantity.text =
                            product.addToCart?.enteredQuantity?.toString() ?: "1"

                        // long description
                        if(product.fullDescription?.isEmpty() == false) {
                            val productDescLayout = vsProductDescLayout?.inflate()
                            productDescLayout?.tvProductName?.text = DbHelper.getString(Const.PRODUCT_DESCRIPTION)
                            product.fullDescription.let { text ->
                                productDescLayout?.tvProductDescription?.apply {
                                    settings?.javaScriptEnabled = true
                                    //settings?.loadWithOverviewMode = true
                                    //settings?.useWideViewPort = true
                                    webViewClient = WebViewClient()

                                    show(text)
                                }
                            }
                            hd14.visibility = View.VISIBLE
                        }

                        // download sample
                        if(product.hasSampleDownload == true) {
                            productRatingLayout?.ivSampleDownload?.let {
                                it.visibility = View.VISIBLE
                                it.setOnClickListener {
                                    // TODO handle sample download
                                }
                            }
                        }

                        // setup product attributes
                        customAttributeManager =
                            CustomAttributeManager(
                                attributes = product.productAttributes,
                                attributeViewHolder = attrViewHolder,
                                attributeValueHolder = bottomSheetLayout.attributeValueHolder,
                                bsBehavior = bsBehavior
                            )

                        customAttributeManager?.attachAttributesToFragment()

                        // Price calculation not needed for Products with manual price input i.e (Donation)
                        if (product.addToCart?.customerEntersPrice == false) {
                            customAttributeManager?.setupProductPriceCalculation(
                                product.productPrice,
                                productPriceLayout.tvDiscountPrice
                            )
                        }


                        // Gift Card
                        if(product.giftCard?.isGiftCard == true) {
                            populateGiftCardSection()
                        }


                        // Rental Product
                        if(product.isRental == true) {
                            populateRentalProductSection()
                        }

                        // Associated Product

                        if(product.associatedProducts?.isNotEmpty() == true) {
                            populateAssociatedProductList(product.associatedProducts)

                            // Hide unrelated layouts
                            productPriceLayout.visibility = View.GONE
                            productQuantityLayout.visibility = View.GONE
                            addtoCartLayout.visibility = View.GONE
                            btnAddToWishList.visibility = View.GONE

                            // Hide horizontal dividers
                            hd11.visibility = View.GONE
                            hd12.visibility = View.GONE

                            productDetailsScrollView.visibility = View.VISIBLE

                            return@Observer
                        } else {
                            addtoCartLayout.visibility = View.VISIBLE
                            btnAddToWishList.visibility = View.VISIBLE
                        }
                        productDetailsScrollView.visibility = View.VISIBLE
                    }
                })

            isLoadingLD.observe(
                viewLifecycleOwner,
                Observer { isShowLoader ->  showHideLoader(isShowLoader) })

            addToCartResponseLD.observe(
                viewLifecycleOwner,
                Observer { data ->
                    val response = data.getContentIfNotHandled() ?: return@Observer

                    val totalCartItems = response.data.totalShoppingCartProducts
                    updateTopCart(totalCartItems)
                    toast(response.message)

                    if (gotoCartPage) {
                        gotoCartPage = false

                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
                            (requireActivity() as BaseActivity).goMenuItemFragment(CartFragment())
                    }

                })

            quantityLiveData.observe(
                viewLifecycleOwner,
                Observer { quantity ->
                    productQuantityLayout.tvQuantity.text = quantity.toString()
                })

            relatedProductsLD.observe(viewLifecycleOwner,
                Observer { list ->
                    populateRelatedProductList(list)
                })

            similarProductsLD.observe(viewLifecycleOwner,
                Observer { list ->
                    populateSimilarProductList(list)
                })

            viewModel.addedToWishListLD.observe(viewLifecycleOwner, Observer { action ->
                action?.getContentIfNotHandled()?.let { product ->
                    replaceFragmentSafely(newInstance(product.id?.toLong() ?: -1L, product.name))
                }
                blockingLoader.hideDialog()
            })
        }

    }

    private fun populateGiftCardSection() {
        if(vsGiftCardLayout == null) return

        val view = vsGiftCardLayout.inflate()

        view.etRecipientName.hint = DbHelper.getString(Const.PRODUCT_GIFT_CARD_RECIEPIENT)
        view.etYourName.hint = DbHelper.getString(Const.PRODUCT_GIFT_CARD_SENDER)
        view.etMessage.hint = DbHelper.getString(Const.PRODUCT_GIFT_CARD_MESSAGE)

        hd15.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun populateRentalProductSection() {
        if (vsRentalProduct == null) return

        val rentalProductLayout = vsRentalProduct.inflate()

        btnBuyNow.text = DbHelper.getString(Const.PRODUCT_BTN_RENT_NOW)
        hd13.visibility = View.VISIBLE

        val calender: Calendar = Calendar.getInstance()

        rentalProductLayout.tvLayoutTitle.text = DbHelper.getString(Const.PRODUCT_RENT)
        rentalProductLayout.etRentFrom.hint = DbHelper.getString(Const.PRODUCT_RENTAL_START)
        rentalProductLayout.etRentFrom.setOnClickListener{

            val dialog = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    rentalProductLayout.etRentFrom.text = TextUtils().getFormattedDate(d,m,y)
                    rentalProductLayout.etRentTo.text = TextUtils().getFormattedDate(d,m,y)

                    (viewModel as ProductDetailViewModel).setRentDate(d, m, y, true)
                },
                calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate = (viewModel as ProductDetailViewModel).getRentDate(true)
            dialog.show()
        }

        rentalProductLayout.etRentTo.hint = DbHelper.getString(Const.PRODUCT_RENTAL_END)
        rentalProductLayout.etRentTo.setOnClickListener{

            if (rentalProductLayout.etRentFrom.text.isNullOrEmpty()) {
                //toast("Select start date")
                return@setOnClickListener
            }

            val dialog = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    rentalProductLayout.etRentTo.text = "$d / $m / $y"
                    (viewModel as ProductDetailViewModel).setRentDate(d, m, y, false)
                },
                calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate = (viewModel as ProductDetailViewModel).getRentDate(false)
            dialog.show()
        }
    }

    private fun populateAssociatedProductList(list: List<ProductDetail>) {
        if (list.isEmpty() || vsAssociatedProduct==null)
            return

        val associatedProductLayout = vsAssociatedProduct.inflate()

        associatedProductLayout.tvProductName.text = DbHelper.getString(Const.PRODUCT_GROUPED_PRODUCT)

        associatedProductLayout.rvFeaturedProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            //addItemDecoration(RecyclerViewMargin(5, 1, isVertical = true))
            setHasFixedSize(true)

            val clickListener = object : ItemClickListener<ProductDetail> {
                override fun onClick(view: View, position: Int, data: ProductDetail) {

                    when (view.id) {
                        R.id.itemView ->
                            replaceFragmentSafely(newInstance(data))

                        R.id.ivAddToWishList, R.id.ivAddToCart ->
                            if(data.productAttributes.isNullOrEmpty()) {
                                addToCartClickAction(data.id ?: -1,
                                    data.addToCart?.enteredQuantity?.toString() ?: "1",
                                    cart = view.id == R.id.ivAddToCart)
                            } else {
                                replaceFragmentSafely(newInstance(data))
                            }
                    }
                }
            }

            adapter = AssociatedProductAdapter(list, clickListener)
        }
    }

    private fun populateRelatedProductList(list: List<ProductSummary>) {

        if (list.isNullOrEmpty() || vsRelatedProduct==null)
            return

        val featuredProductLayout = vsRelatedProduct.inflate()

        featuredProductLayout.tvProductName.text = DbHelper.getString(Const.PRODUCT_RELATED_PRODUCT)

        featuredProductLayout.rvFeaturedProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, listItemClickListener)
        }
    }

    private fun populateSimilarProductList(list: List<ProductSummary>) {

        if (list.isNullOrEmpty() || vsSimilarProduct ==null)
            return

        val similarProductLayout = vsSimilarProduct.inflate()

        similarProductLayout.tvProductName.text = DbHelper.getString(Const.PRODUCT_ALSO_PURCHASED)

        similarProductLayout.rvFeaturedProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, listItemClickListener)
        }
    }

    private fun initView(productId: Long) {
        // to avoid auto scrolling of scrollview
        focusStealer.requestFocus()

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        /*bsBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })*/

        bottomSheetLayout.tvDone.text = DbHelper.getString(Const.COMMON_DONE)
        bottomSheetLayout.tvDone.setOnClickListener(this)
        productQuantityLayout.btnMinus.setOnClickListener(this)
        productQuantityLayout.btnPlus.setOnClickListener(this)

        listItemClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {
                if(data.id == null) return

                when(view.id) {

                    R.id.itemView ->
                        replaceFragmentSafely(newInstance(data.id.toLong(), data.name))

                    R.id.ivAddToFav ->
                        addProductToWishList(data)
                }
            }
        }

        btnAddToCart.text = DbHelper.getString(Const.PRODUCT_BTN_ADDTOCART)
        btnAddToCart?.setOnClickListener {
            if((viewModel as ProductDetailViewModel).productLiveData.value?.addToCart?.disableBuyButton == true) {
                toast(DbHelper.getString(Const.PRODUCT_BUY_DISABLED))
                return@setOnClickListener
            }

            addToCartClickAction(productId, productQuantityLayout?.tvQuantity?.text.toString(), cart = true)
        }

        btnAddToWishList?.tvLabel?.text = DbHelper.getString(Const.PRODUCT_BTN_ADDTO_WISHLIST)
        btnAddToWishList?.setOnClickListener {
            if((viewModel as ProductDetailViewModel).productLiveData.value?.addToCart?.disableWishlistButton == true) {
                toast(DbHelper.getString(Const.PRODUCT_WISHLIST_DISABLED))
                return@setOnClickListener
            }

            addToCartClickAction(productId, productQuantityLayout?.tvQuantity?.text.toString(), cart = false)
        }

        btnBuyNow.text = DbHelper.getString(Const.PRODUCT_BTN_BUY_NOW)
        btnBuyNow?.setOnClickListener {

            if((viewModel as ProductDetailViewModel).productLiveData.value?.addToCart?.disableBuyButton == true) {
                toast(DbHelper.getString(Const.PRODUCT_BUY_DISABLED))
                return@setOnClickListener
            }

            (viewModel as ProductDetailViewModel).gotoCartPage = true
            addToCartClickAction(productId, productQuantityLayout?.tvQuantity?.text.toString(), cart = true)
        }

    }

    private fun addToCartClickAction(productId: Long, quantity: String, cart: Boolean) {

        // Add manually entered price to Product model
        val product = (viewModel as ProductDetailViewModel).productLiveData.value

        if (product?.addToCart?.customerEntersPrice == true) {

            productPriceLayout.labelEnterPrice.text = DbHelper.getString(Const.ENTER_PRICE)
            val enteredPriceStr = EditTextUtils().showToastIfEmpty(productPriceLayout.etPrice,
                DbHelper.getString(Const.ENTER_PRICE_REQ)) ?: return

            (viewModel as ProductDetailViewModel)
                .productLiveData.value?.addToCart
                ?.customerEnteredPrice = enteredPriceStr.toDoubleOrNull()
        }


        if (product?.giftCard?.isGiftCard == true) {
            (viewModel as ProductDetailViewModel)
                .productLiveData.value?.giftCard?.apply{
                    senderName = EditTextUtils().getString(etYourName)
                    recipientName = EditTextUtils().getString(etRecipientName)
                    message = EditTextUtils().getString(etMessage)
                }
        }

        (viewModel as ProductDetailViewModel).addProductToCartModel(
            productId,
            quantity,
            customAttributeManager?.getFormData(Api.productAttributePrefix) ?: KeyValueFormData(),
            model,
            if(cart) Api.typeShoppingCart else Api.typeWishList
        )
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btnPlus -> (viewModel as ProductDetailViewModel).incrementQuantity()
            R.id.btnMinus -> (viewModel as ProductDetailViewModel).decrementQuantity()
            R.id.tvDone -> {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                //productAttributeView?.onBottomSheetClose()
            }
        }
    }

    companion object {
        @JvmStatic
        private val PRODUCT_ID = "productId"
        @JvmStatic
        private val PRODUCT_NAME = "productName"
        @JvmStatic
        private val ASSOCIATED_PRODUCT = "associatedProduct"

        @JvmStatic
        private var associatedProduct: ProductDetail? = null

        @JvmStatic
        fun newInstance(productId: Long, productName: String?): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle()
            args.putLong(PRODUCT_ID, productId)
            args.putString(PRODUCT_NAME, productName)
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(product: ProductDetail): ProductDetailFragment {

            associatedProduct = product

            val fragment = ProductDetailFragment()

            fragment.arguments = Bundle().apply {
                putBoolean(ASSOCIATED_PRODUCT, true)
                putLong(PRODUCT_ID, product.id ?: -1)
                putString(PRODUCT_NAME, product.name)
            }

            return fragment
        }
    }
}