package com.bs.ecommerce.product

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
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.model.data.SubCategory
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.ProductListModelImpl
import com.bs.ecommerce.product.viewModel.ProductListViewModel
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.inflate
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlin.math.floor

class ProductListFragment : BaseFragment() {

    private lateinit var model: ProductListModel
    private lateinit var categoryName: String
    private lateinit var getBy: String
    private var categoryId: Int = 0
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var subcategoryPopupWindow: ListPopupWindow
    private lateinit var productClickListener: ItemClickListener<ProductSummary>

    private var viewCreated = false
    private var rootView: View? = null

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel =
        ProductListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (rootView == null)
            rootView = container?.inflate(R.layout.fragment_product_list)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            val bundle = this.arguments
            if (bundle != null) {
                categoryId = bundle.getInt(CATEGORY_ID, categoryId)
                categoryName = bundle.getString(CATEGORY_NAME, "")
                getBy = bundle.getString(GET_PRODUCT_BY, "")
            }

            calculateAutomaticGridColumn()

            model = ProductListModelImpl()
            viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)

            if (getBy == GetBy.CATEGORY.name)
                (viewModel as ProductListViewModel).getProductByCategory(categoryId.toLong(), model)
            if (getBy == GetBy.MANUFACTURER.name)
                (viewModel as ProductListViewModel).getProductByManufacturer(
                    categoryId.toLong(),
                    model
                )


            productClickListener = object : ItemClickListener<ProductSummary> {
                override fun onClick(view: View, position: Int, data: ProductSummary) {

                    if (data.id == null) return

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.layoutFrame,
                            ProductDetailFragment.newInstance(data.id.toLong())
                        )
                        .addToBackStack(
                            ProductDetailFragment::class.java.simpleName
                        ).commit()
                }
            }

            viewCreated = true
        }

        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {
        val viewModel = viewModel as ProductListViewModel

        viewModel.productLiveData.observe(viewLifecycleOwner, Observer { data ->

            rvProductList.adapter = ProductListAdapter(data.products!!, productClickListener)

            initSubcategoryPopupWindow(data.subCategories)

            llButtonHolder.visibility =
                if (data.subCategories.isNullOrEmpty()) View.VISIBLE else View.GONE
        })

        viewModel.manufacturerLD.observe(viewLifecycleOwner, Observer { manufacturer ->
            rvProductList.adapter = ProductListAdapter(manufacturer.products!!, productClickListener)

            llButtonHolder.visibility = View.VISIBLE
            btnBrand.visibility = View.GONE
        })

        viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

        viewModel.toastMessageLD.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotEmpty())
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initSubcategoryPopupWindow(subCatList: List<SubCategory>?) {

        if (subCatList.isNullOrEmpty()) {
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
                .replace(
                    R.id.layoutFrame,
                    newInstance(
                        subCatList[position].name!!,
                        subCatList[position].id!!,
                        GetBy.CATEGORY
                    )
                )
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


    enum class GetBy {
        CATEGORY, MANUFACTURER
    }

    companion object {
        @JvmStatic
        private val CATEGORY_NAME = "categoryName"

        @JvmStatic
        private val CATEGORY_ID = "categoryId"

        @JvmStatic
        private val GET_PRODUCT_BY = "getProductBy"

        @JvmStatic
        fun newInstance(categoryName: String, categoryId: Int, getBy: GetBy): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle()
            args.putString(CATEGORY_NAME, categoryName)
            args.putInt(CATEGORY_ID, categoryId)
            args.putString(GET_PRODUCT_BY, getBy.name)
            fragment.arguments = args
            return fragment
        }
    }
}