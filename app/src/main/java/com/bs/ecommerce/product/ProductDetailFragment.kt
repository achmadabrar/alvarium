package com.bs.ecommerce.product

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.adapter.AssociatedProductAdapter
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.viewModel.ProductDetailViewModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.featured_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.tvProductName
import kotlinx.android.synthetic.main.product_price_layout.view.*
import kotlinx.android.synthetic.main.product_quantity.view.*
import kotlinx.android.synthetic.main.rental_product_layout.view.*
import kotlinx.android.synthetic.main.slider.view.*
import java.util.*


class ProductDetailFragment : BaseFragment(), View.OnClickListener {

    private lateinit var bsBehavior: BottomSheetBehavior<*>
    private lateinit var model: ProductDetailModel
    private lateinit var listItemClickListener: ItemClickListener<ProductSummary>
    private var customAttributeManager: CustomAttributeManager? = null

    override fun getFragmentTitle() = R.string.title_product

    override fun getLayoutId(): Int = R.layout.fragment_product_detail

    override fun getRootLayout(): RelativeLayout = productDetailsRootLayout

    override fun createViewModel(): BaseViewModel =
        ProductDetailViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments?.getLong(PRODUCT_ID)==null) {
            toast(R.string.invalid_id)
            requireActivity().supportFragmentManager.popBackStack()
            return
        }

        if(!viewCreated) {

            val productId: Long = arguments?.getLong(PRODUCT_ID)!!

            initView(productId)

            model = ProductDetailModelImpl()
            viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

            (viewModel as ProductDetailViewModel).apply {

                getProductDetail(productId, model)

                getRelatedProducts(
                    productId,
                    resources.getDimensionPixelSize(R.dimen.product_item_size),
                    model
                )

                getSimilarProducts(
                    productId,
                    resources.getDimensionPixelSize(R.dimen.product_item_size),
                    model
                )
            }
        }

        setLiveDataListeners()
    }

    private fun setLiveDataListeners() {

        (viewModel as ProductDetailViewModel).apply {

            productLiveData.observe(
                viewLifecycleOwner,
                Observer { product ->

                    // slider image
                    val imageSlider = vsImageSlider?.inflate()

                    val detailsSliderAdapter =
                        DetailsSliderAdapter(requireContext(), product.pictureModels)
                    imageSlider?.view_pager_slider?.adapter = detailsSliderAdapter
                    imageSlider?.view_pager_slider?.currentItem = 0
                    imageSlider?.circle_indicator?.setViewPager(imageSlider.view_pager_slider)

                    imageSlider?.circle_indicator?.pageColor =
                        ContextCompat.getColor(activity!!, R.color.white)
                    imageSlider?.circle_indicator?.fillColor =
                        ContextCompat.getColor(activity!!, R.color.darkOrGray)

                    detailsSliderAdapter.setOnSliderClickListener(object :
                        DetailsSliderAdapter.OnSliderClickListener {
                        override fun onSliderClick(view: View, sliderPosition: Int) {
                            // TODO
                            /*FullScreenImageActivity.sliderPosition = sliderPosition
                            FullScreenImageActivity.pictureModels = detail.pictureModels
                            val intent = Intent(activity, FullScreenImageActivity::class.java)
                            startActivity(intent)*/
                        }
                    })

                    // short description
                    val productNameLayout = vsProductNameLayout?.inflate()
                    productNameLayout?.tvProductName?.text = product.name
                    product.shortDescription?.let {
                        productNameLayout?.tvProductDescription?.show(
                            it,
                            R.color.fragment_background
                        )
                    }

                    // product price
                    productPriceLayout.tvOriginalPrice.apply {
                        if (product.productPrice?.oldPrice == null) {
                            visibility = View.GONE
                        } else {
                            text = product.productPrice?.oldPrice
                            paintFlags =
                                productPriceLayout.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }

                    //productPriceLayout.tvDiscountPercent.text = "40% Off"

                    tvAvailability.text = product.stockAvailability

                    productQuantityLayout.tvQuantity.text = "1"

                    // long description
                    val productDescLayout = vsProductDescLayout?.inflate()
                    productDescLayout?.tvProductName?.text = getString(R.string.description)
                    product.fullDescription?.let {
                        productDescLayout?.tvProductDescription?.show(
                            it,
                            R.color.fragment_background
                        )
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

                    customAttributeManager?.setupProductPriceCalculation(
                        product?.productPrice,
                        productPriceLayout.tvDiscountPrice
                    )


                    // Rental Product
                    if(product.isRental == true) {
                        populateRentalProductSection(product)
                    }

                    // Associated Product
                    val isGroupProduct = product.associatedProducts?.isNotEmpty() ?: false

                    if(isGroupProduct) {
                        populateAssociatedProductList(product.associatedProducts!!)

                        // Hide unrelated layouts
                        productPriceLayout.visibility = View.GONE
                        productQuantityLayout.visibility = View.GONE
                        addtoCartLayout.visibility = View.GONE

                        // Hide horizontal dividers
                        hd11.visibility = View.GONE
                        hd12.visibility = View.GONE

                        productDetailsScrollView.visibility = View.VISIBLE

                        return@Observer
                    } else {
                        addtoCartLayout.visibility = View.VISIBLE
                    }

                    productDetailsScrollView.visibility = View.VISIBLE

                })

            isInvalidProductLD.observe(
                viewLifecycleOwner,
                Observer { isInvalid ->

                    if (isInvalid) {
                        toast(getString(R.string.invalid_barcode))
                        requireActivity().supportFragmentManager.popBackStackImmediate()
                    }

                })

            isLoadingLD.observe(
                viewLifecycleOwner,
                Observer { isShowLoader ->

                    if (isShowLoader)
                        showLoading()
                    else
                        hideLoading()
                })

            cartProductsCountLD.observe(
                viewLifecycleOwner,
                Observer { count ->

                    MyApplication.setCartCounter(count)
                    activity?.let {  (it as BaseActivity).updateHotCount(count)    }
                })

            addToCartResponseLD.observe(
                viewLifecycleOwner,
                Observer { response ->

                    if(response.errorList.isNotEmpty())
                        toast(response.errorsAsFormattedString)
                    else
                        toast(response.message)
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
                if (action == 1) {
                    replaceFragmentSafely(CartFragment())
                }
                blockingLoader.hideDialog()
            })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun populateRentalProductSection(product: ProductDetail) {
        if (vsRentalProduct == null) return

        val rentalProductLayout = vsRentalProduct.inflate()

        btnBuyNow.text = getString(R.string.rent_now)
        hd13.visibility = View.VISIBLE

        val calender: Calendar = Calendar.getInstance()

        rentalProductLayout.etRentFrom.setOnClickListener{

            val dialog = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    rentalProductLayout.etRentFrom.text = "$d / $m / $y"
                    rentalProductLayout.etRentTo.text = "$d / $m / $y"

                    (viewModel as ProductDetailViewModel).setRentDate(d, m, y, true)
                },
                calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate = (viewModel as ProductDetailViewModel).getRentDate(true)
            dialog.show()
        }

        rentalProductLayout.etRentTo.setOnClickListener{

            if (rentalProductLayout.etRentFrom.text.isNullOrEmpty()) {
                toast("Select start date")
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

        associatedProductLayout.tvProductName.text = getString(R.string.associated_product)

        associatedProductLayout.rvFeaturedProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            //addItemDecoration(RecyclerViewMargin(5, 1, isVertical = true))
            setHasFixedSize(true)

            val clickListener = object: ItemClickListener<ProductDetail> {
                override fun onClick(view: View, position: Int, data: ProductDetail) {

                    when(view.id) {
                        R.id.itemView ->
                            toast(data.name ?: "")

                        R.id.ivAddToWishList ->
                            addProductToWishList(data.id!!)

                        R.id.ivAddToCart ->
                            addToCartClickAction(data.id!!, data.quantity.toString())
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

        featuredProductLayout.tvProductName.text = getString(R.string.related_products)

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

        similarProductLayout.tvProductName.text = getString(R.string.people_also_purchase)

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

        bottomSheetLayout.tvDone.setOnClickListener(this)
        productQuantityLayout.btnMinus.setOnClickListener(this)
        productQuantityLayout.btnPlus.setOnClickListener(this)

        listItemClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {
                if(data.id == null) return

                when(view.id) {

                    R.id.itemView ->
                        replaceFragmentSafely(newInstance(data.id.toLong()))

                    R.id.ivAddToFav ->
                        addProductToWishList(data.id.toLong())
                }
            }
        }

        btnAddToCart?.setOnClickListener {
            addToCartClickAction(productId, productQuantityLayout?.tvQuantity?.text.toString())
        }
    }

    private fun addToCartClickAction(productId: Long, quantity: String) {
        (viewModel as ProductDetailViewModel).addProductToCartModel(
            productId,
            quantity,
            customAttributeManager?.getFormData(Api.productAttributePrefix) ?: KeyValueFormData(),
            model
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
        fun newInstance(productId: Long): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle()
            args.putLong(PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }
}