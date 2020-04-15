package com.bs.ecommerce.product.ui

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ListPopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.product.*
import com.bs.ecommerce.product.data.ProductSummary
import com.bs.ecommerce.product.data.SubCategory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.item_featured_product.view.*
import kotlin.math.floor

class ProductListFragment : BaseFragment() {

    private lateinit var model: ProductListModel
    private lateinit var categoryName: String
    private var categoryId: Int = 0
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var subcategoryPopupWindow: ListPopupWindow

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel = ProductListViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            categoryId = bundle.getInt(CATEGORY_ID, categoryId)
            categoryName = bundle.getString(CATEGORY_NAME, "")
        }

        calculateAutomaticGridColumn()

        model = ProductListModelImpl()
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)

        (viewModel as ProductListViewModel).getProductDetail(categoryId.toLong(), model)

        setLiveDataListeners()
    }

    private fun setLiveDataListeners() {
        (viewModel as ProductListViewModel).productLiveData.observe(
            requireActivity(),
            Observer { data ->

                rvProductList.adapter = TempAdapter(data.products!!)

                initSubcategoryPopupWindow(data.subCategories)

                llButtonHolder.visibility =
                    if (data.subCategories.isNullOrEmpty()) View.VISIBLE else View.GONE
            })

        (viewModel as ProductListViewModel).isLoadingLD.observe(
            requireActivity(),
            Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

        (viewModel as ProductListViewModel).toastMessageLD.observe(
            requireActivity(),
            Observer { message ->
                if (message.isNotEmpty())
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            })
    }

    private fun initSubcategoryPopupWindow(subCatList: List<SubCategory>?) {

        if(subCatList.isNullOrEmpty()) {
            categoryNameTextView.visibility = View.GONE
            return
        }

        categoryNameTextView.visibility = View.VISIBLE
        categoryNameTextView.text = categoryName

        subcategoryPopupWindow = ListPopupWindow(requireContext())

        subcategoryPopupWindow.anchorView = categoryNameTextView
        subcategoryPopupWindow.isModal = true
        subcategoryPopupWindow.setBackgroundDrawable(ColorDrawable(0))

        subcategoryPopupWindow.setAdapter(SubCategoryAdapter(subCatList))

        categoryNameTextView?.setOnClickListener {
            if (subcategoryPopupWindow.isShowing) {
                subcategoryPopupWindow.dismiss()
            } else {
                subcategoryPopupWindow.show()
            }
        }

        subcategoryPopupWindow.setOnItemClickListener { parent, view, position, id ->
            subcategoryPopupWindow.dismiss()

            requireActivity().supportFragmentManager.beginTransaction()
                .add(
                    R.id.layoutFrame,
                    newInstance(subCatList[position].name!!, subCatList[position].id!!)
                )
                .hide(this)
                .addToBackStack(null)
                .commit()
        }

    }

    private fun calculateAutomaticGridColumn() {
        layoutManager = GridLayoutManager(requireContext(), 2)

        rvProductList.layoutManager = layoutManager

        rvProductList.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rvProductList.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                    val viewWidth = rvProductList.measuredWidth
                    val cardViewWidth =
                        requireContext().resources?.getDimension(R.dimen.product_item_size)

                    val newSpanCount = floor((viewWidth / cardViewWidth!!).toDouble()).toInt()

                    updateColumnPerRow(newSpanCount)

                    val decoration = object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            super.getItemOffsets(outRect, view, parent, state)
                            outRect.set(15, 15, 15, 15)
                        }
                    }

                    rvProductList.addItemDecoration(decoration)
                }
            })
    }

    private fun updateColumnPerRow(spanCount: Int) {
        layoutManager.spanCount = spanCount
        layoutManager.requestLayout()
    }

    //---------------------------------------

    inner class TempAdapter(
        private val productsList: List<ProductSummary>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_featured_product, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = productsList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tvProductName.text = productsList[position].name
            holder.itemView.tvProductPrice.text = productsList[position].productPrice?.price

            holder.itemView.ratingBar.rating =
                (productsList[position].reviewOverviewModel?.ratingSum ?: 0).toFloat()

            Picasso.with(holder.itemView.context).load(
                productsList[position].defaultPictureModel?.imageUrl
                    ?: "https://picsum.photos/300/300"
            ).fit().centerInside().into(holder.itemView.ivProductThumb)

            holder.itemView.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.layoutFrame, ProductDetailFragment.newInstance(productsList[position].id?.toLong() ?: 1))
                    .hide(this@ProductListFragment)
                    .addToBackStack(ProductDetailFragment::class.java.simpleName
                    ).commit()
            }
        }

    }

    companion object {
        @JvmStatic
        private val CATEGORY_NAME = "categoryName"

        @JvmStatic
        private val CATEGORY_ID = "categoryId"

        @JvmStatic
        fun newInstance(categoryName: String, categoryId: Int): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle()
            args.putString(CATEGORY_NAME, categoryName)
            args.putInt(CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
    }
}