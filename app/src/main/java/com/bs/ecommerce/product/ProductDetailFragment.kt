package com.bs.ecommerce.product

import android.R.attr.numColumns
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.TextUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.featured_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.item_featured_product.view.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.tvProductName
import kotlinx.android.synthetic.main.product_price_layout.view.*
import kotlinx.android.synthetic.main.product_quantity.view.*
import kotlinx.android.synthetic.main.slider.view.*


class ProductDetailFragment : BaseFragment() {

    private lateinit var bsBehavior: BottomSheetBehavior<*>
    private lateinit var model: ProductDetailModel

    override fun getLayoutId(): Int = R.layout.fragment_product_detail

    override fun getRootLayout(): RelativeLayout = productDetailsRootLayout

    override fun createViewModel(): BaseViewModel = ProductDetailViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        model = ProductDetailModelImpl()
        viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        (viewModel as ProductDetailViewModel).getProductDetail(model)

        setLiveDataListeners()
    }

    private fun setLiveDataListeners() {
        (viewModel as ProductDetailViewModel).productLiveData.observe(
            requireActivity(),
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
                productNameLayout.tvProductDescription.text = product.shortDescription

                productPriceLayout.tvDiscountPrice.text = product.productPrice?.price ?: "$0"
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
                val productAttributeView =
                    ProductAttributeView(requireContext(), product, bottomSheetLayout, bsBehavior)
                for (i in productAttributeView.getAttrViews()) {
                    attrViewHolder.addView(i)
                }

            })

        (viewModel as ProductDetailViewModel).isLoadingLD.observe(
            requireActivity(),
            Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
    }

    private fun initView() {

        //
        val items: MutableList<FeaturedProduct> = mutableListOf()

        for (i in 1..5) {
            items.add(FeaturedProduct("Product $i", "$${i * 167}"))
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        similarProductList.rvFeaturedProduct?.setHasFixedSize(true)
        similarProductList.rvFeaturedProduct?.layoutManager = layoutManager

        val decoration = RecyclerViewMargin(15, numColumns, false)
        similarProductList.rvFeaturedProduct?.addItemDecoration(decoration)

        similarProductList.rvFeaturedProduct?.adapter = TempAdapter(items)


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

        bottomSheetLayout.tvDone.setOnClickListener {
            bsBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    inner class TempAdapter(
        private val productsList: List<FeaturedProduct>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_featured_product, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int = productsList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyViewHolder) {
                holder.itemView.tvProductName.text = productsList[position].name
                holder.itemView.tvProductPrice.text = productsList[position].price

                Picasso.with(holder.itemView.context).load("https://picsum.photos/300/300")
                    .fit().centerInside().into(holder.itemView.ivProductThumb)
            }
        }

    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    inner class FeaturedProduct(
        val name: String,
        val price: String
    )
}