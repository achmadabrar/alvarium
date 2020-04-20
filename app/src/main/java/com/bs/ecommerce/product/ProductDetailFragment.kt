package com.bs.ecommerce.product

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.viewModel.ProductDetailViewModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.featured_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.tvProductName
import kotlinx.android.synthetic.main.product_price_layout.view.*
import kotlinx.android.synthetic.main.product_quantity.view.*
import kotlinx.android.synthetic.main.slider.view.*
import kotlin.collections.HashMap


class ProductDetailFragment : BaseFragment(), View.OnClickListener {

    private lateinit var bsBehavior: BottomSheetBehavior<*>
    private lateinit var model: ProductDetailModel
    private var productAttributeView: ProductAttributeView? = null
    private lateinit var mainViewModel: MainViewModel

    var selectedAttributeMap : MutableMap<Long, MutableList<AttributeControlValue>> = HashMap()


    override fun getLayoutId(): Int = R.layout.fragment_product_detail

    override fun getRootLayout(): RelativeLayout = productDetailsRootLayout

    override fun createViewModel(): BaseViewModel =
        ProductDetailViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getLong(PRODUCT_ID) ?: 1

        initView()

        model = ProductDetailModelImpl()
        viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        (viewModel as ProductDetailViewModel).getProductDetail(productId, model)

        setLiveDataListeners()

        btnAddToCart?.setOnClickListener {

            (viewModel as ProductDetailViewModel).addProductToCartModel(selectedAttributeMap, model)

        }
    }

    private fun setLiveDataListeners() {


        (viewModel as ProductDetailViewModel).selectedAttrLD.observe(
            viewLifecycleOwner,
            Observer { selectedAttributeMapLD ->

                selectedAttributeMap.clear()
                selectedAttributeMap = selectedAttributeMapLD
            })




        (viewModel as ProductDetailViewModel).productLiveData.observe(
            viewLifecycleOwner,
            Observer { product ->
                productDetailsRootLayout.visibility = View.VISIBLE

                // slider image
                val detailsSliderAdapter =
                    DetailsSliderAdapter(requireContext(), product.pictureModels)
                imageSlider.view_pager_slider?.adapter = detailsSliderAdapter
                imageSlider.view_pager_slider?.currentItem = 0
                imageSlider.circle_indicator?.setViewPager(imageSlider.view_pager_slider)

                imageSlider.circle_indicator?.pageColor =
                    ContextCompat.getColor(activity!!, R.color.white)
                imageSlider.circle_indicator?.fillColor =
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
                productNameLayout.tvProductName.text = product.name
                productNameLayout.tvProductDescription.text =
                    TextUtils().getHtmlFormattedText(product.shortDescription)

                productPriceLayout.tvOriginalPrice.text = product.productPrice?.oldPrice ?: "$0"
                productPriceLayout.tvOriginalPrice.paintFlags =
                    productPriceLayout.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                productPriceLayout.tvDiscountPercent.text = "40% Off"

                tvAvailability.text = product.stockAvailability

                productQuantityLayout.tvQuantity.text = "1"

                // long description
                productDescLayout.tvProductName.text = "Description"
                productDescLayout.tvProductDescription.text =
                    TextUtils().getHtmlFormattedText(product.fullDescription)

                // setup product attributes
                productAttributeView =
                    ProductAttributeView(requireContext(), viewModel as ProductDetailViewModel,
                        bottomSheetLayout, bsBehavior)
                for (i in productAttributeView!!.getAttrViews()) {
                    attrViewHolder.addView(i)
                }

            })

        (viewModel as ProductDetailViewModel).isLoadingLD.observe(
            viewLifecycleOwner,
            Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

        (viewModel as ProductDetailViewModel).quantityLiveData.observe(
            viewLifecycleOwner,
            Observer { quantity ->
                productQuantityLayout.tvQuantity.text = quantity.toString()
            })

        (viewModel as ProductDetailViewModel).toastMessageLD.observe(
            viewLifecycleOwner,
            Observer { message ->
                if(message.isNotEmpty())
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            })

        (viewModel as ProductDetailViewModel).productPriceLD.observe(
            viewLifecycleOwner,
            Observer { price ->
                productPriceLayout.tvDiscountPrice.text = "$%.2f".format(price)
            })

        (viewModel as ProductDetailViewModel).selectedAttrLD.observe(
            viewLifecycleOwner,
            Observer { attrMap ->

                for(i in attrMap.keys) {
                    val view = attrViewHolder.findViewWithTag<View>(i)
                    val textView = view.findViewById<TextView>(R.id.tvSelectedAttr)

                    val selectedAttr = attrMap[i]

                    if(selectedAttr.isNullOrEmpty()) {
                        textView?.text = getString(R.string.select)
                    } else {
                        textView?.text = attrMap[i]?.get(0)?.name
                    }
                }
            })

        mainViewModel.featuredProductListLD.observe(viewLifecycleOwner,
            Observer { list ->
                populateFeaturedProductList(list)
            })
    }

    private fun populateFeaturedProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            featuredProductLayout.visibility = View.GONE
            return
        }

        featuredProductLayout.visibility = View.VISIBLE
        featuredProductLayout.tvProductName.text = getString(R.string.featured_products)
        featuredProductLayout.tvSeeAll.setOnClickListener {
            toast("Sell All")
        }

        featuredProductLayout.rvFeaturedProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, object : ItemClickListener<ProductSummary> {
                override fun onClick(view: View, position: Int, data: ProductSummary) {
                    toast("Not yet implemented")
                }
            })
        }
    }

    private fun initView() {

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bsBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
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
        })

        bottomSheetLayout.tvDone.setOnClickListener(this)
        productQuantityLayout.btnMinus.setOnClickListener(this)
        productQuantityLayout.btnPlus.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.btnPlus -> (viewModel as ProductDetailViewModel).incrementQuantity()
            R.id.btnMinus -> (viewModel as ProductDetailViewModel).decrementQuantity()
            R.id.tvDone -> {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                productAttributeView?.onBottomSheetClose()
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