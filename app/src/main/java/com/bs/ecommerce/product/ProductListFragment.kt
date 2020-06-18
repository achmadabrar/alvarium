package com.bs.ecommerce.product

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.adapter.ProductListAdapter
import com.bs.ecommerce.product.adapter.SubCategoryAdapter
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.ProductListModelImpl
import com.bs.ecommerce.product.model.data.PagingFilteringContext
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.model.data.SubCategory
import com.bs.ecommerce.product.viewModel.ProductListViewModel
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.sort_option_bottom_sheet.view.*
import kotlin.math.floor


class ProductListFragment : BaseFragment() {

    private lateinit var model: ProductListModel
    private lateinit var categoryName: String
    private lateinit var getBy: String
    private var categoryId: Int = 0
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var subcategoryPopupWindow: ListPopupWindow
    private lateinit var productClickListener: ItemClickListener<ProductSummary>
    private lateinit var productListAdapter: ProductListAdapter

    private val sortOptionDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(requireContext(), R.style.BsDialog)
    }
    
    private lateinit var pageSizeDialog: BottomSheetDialog

    override fun getFragmentTitle() = arguments?.getString(CATEGORY_NAME, "") ?: ""

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel = ProductListViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            val bundle = this.arguments
            if (bundle != null) {
                categoryId = bundle.getInt(CATEGORY_ID, categoryId)
                categoryName = bundle.getString(CATEGORY_NAME, "")
                getBy = bundle.getString(GET_PRODUCT_BY, "")
            }

            initView()

            model = ProductListModelImpl()

            // Explicitly used deprecated library to create viewModel with this fragments scope
            viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)

            getProducts(resetFilters = true)
        }

        setLiveDataListeners()
    }

    override fun onResume() {
        super.onResume()

        arguments?.getString(CATEGORY_NAME)?.let {
            activity?.title = it
        }
    }

    private fun getProducts(resetFilters: Boolean) {
        if (getBy == GetBy.CATEGORY.name)
            (viewModel as ProductListViewModel).getProductByCategory(
                categoryId.toLong(), resetFilters, model
            )
        if (getBy == GetBy.MANUFACTURER.name)
            (viewModel as ProductListViewModel).getProductByManufacturer(
                categoryId.toLong(), resetFilters, model
            )
        if (getBy == GetBy.TAG.name)
            (viewModel as ProductListViewModel).getProductByTag(
                categoryId.toLong(), resetFilters, model
            )
        if (getBy == GetBy.VENDOR.name)
            (viewModel as ProductListViewModel).getProductByVendor(
                categoryId.toLong(), resetFilters, model
            )
    }

    private fun initView() {
        calculateAutomaticGridColumn()

        tvNoProduct.text = DbHelper.getString(Const.COMMON_NO_DATA)

        btnFilter.findViewById<TextView>(R.id.tvFilter).text = DbHelper.getString(Const.FILTER)
        btnFilter.setOnClickListener {
            when (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                true -> {
                    drawerLayout?.closeDrawers()
                }
                false -> {
                    sortOptionDialog.hide()
                    drawerLayout?.openDrawer(GravityCompat.END)
                }
            }
        }

        btnSortBy.findViewById<TextView>(R.id.tvSortBy).text = DbHelper.getString(Const.CATALOG_ORDER_BY)
        btnSortBy.setOnClickListener {
            drawerLayout?.closeDrawers()
            sortOptionDialog.show()
        }

        productClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {

                if (data.id == null) return

                when(view.id) {

                    R.id.itemView ->
                        replaceFragmentSafely(ProductDetailFragment.newInstance(
                            data.id.toLong(), data.name))

                    R.id.ivAddToFav ->
                        addProductToWishList(data)
                }
            }
        }

        productListAdapter = ProductListAdapter(
            productClickListener
        )

        rvProductList?.adapter = productListAdapter

        rvProductList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible == totalItemCount-1
                if (totalItemCount > 0 && endHasBeenReached) {
                    Log.d("nop_", "last item of Product list is visible")
                    getProducts(resetFilters = false)
                }
            }
        })

        // drawer view
        childFragmentManager
            .beginTransaction()
            .replace(R.id.filterFragmentHolder, ProductFilterFragment())
            .commit()
    }

    private fun populateSortOptions(sortOption: PagingFilteringContext?) {

        pageSizeDialog = BottomSheetDialog(requireContext(), R.style.BsDialog)

        val pageSizeHolder:LinearLayout = layoutInflater.inflate(R.layout.sort_option_bottom_sheet, null, false) as LinearLayout
        pageSizeHolder.sortOptionBsTitle.text = DbHelper.getString(Const.CATALOG_ITEMS_PER_PAGE)
        
        // page size selection
        if(sortOption?.allowCustomersToSelectPageSize== true && !sortOption.pageSizeOptions.isNullOrEmpty()) {
            fabPageSize.visibility = View.VISIBLE

            for(i in sortOption.pageSizeOptions) {

                val tv = layoutInflater.inflate(R.layout.custom_attribute_dropdown, null) as TextView
                tv.text = i.text
                tv.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    if (i.selected == true) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                    0
                )
                pageSizeHolder.addView(tv)

                tv.setOnClickListener {
                    PrefSingleton.setPageSize(i.text)

                    pageSizeDialog.hide()
                    applyFilter(i.value)
                }
            }

            pageSizeDialog.setContentView(pageSizeHolder)

            fabPageSize.setOnClickListener {
                pageSizeDialog.show()
            }
        }
        
        // sort option selection
        val sortOptionHolder:LinearLayout = layoutInflater.inflate(R.layout.sort_option_bottom_sheet, null, false) as LinearLayout
        sortOptionHolder.findViewById<TextView>(R.id.sortOptionBsTitle)?.text = DbHelper.getString(Const.CATALOG_ORDER_BY)

        if(sortOption?.allowProductSorting == true && !sortOption.availableSortOptions.isNullOrEmpty()) {

            for(i in sortOption.availableSortOptions) {

                val tv = layoutInflater.inflate(R.layout.custom_attribute_dropdown, null) as TextView
                tv.text = i.text
                tv.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    if (i.selected == true) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                    0
                )
                sortOptionHolder.addView(tv)

                tv.setOnClickListener {
                    sortOptionDialog.hide()
                    applyFilter(i.value)
                }
            }
        } else {
            btnSortBy.visibility = View.GONE
            btn_vertical_divider.visibility = View.GONE
        }

        sortOptionDialog.setContentView(sortOptionHolder)
    }

    private fun setLiveDataListeners() {
        val viewModel = viewModel as ProductListViewModel

        viewModel.productLiveData.observe(viewLifecycleOwner, Observer { data ->

            productListAdapter.addData(data.products, viewModel.shouldAppend)

            if(data.pagingFilteringContext?.pageNumber ?: 1 == 1) {
                initSubcategoryPopupWindow(data.name, data.subCategories)

                tvNoProduct.visibility =
                    if (data.products.isNullOrEmpty()) View.VISIBLE else View.GONE

                populateSortOptions(data.pagingFilteringContext)
                setToolbarTitle(data.name)
            }
        })

        viewModel.tagLiveData.observe(viewLifecycleOwner, Observer { data ->

            productListAdapter.addData(data.products, viewModel.shouldAppend)

            if(data.pagingFilteringContext?.pageNumber ?: 1 == 1) {
                tvNoProduct.visibility =
                    if (data.products.isNullOrEmpty()) View.VISIBLE else View.GONE

                populateSortOptions(data.pagingFilteringContext)
                setToolbarTitle(data.name)
            }
        })

        viewModel.vendorLiveData.observe(viewLifecycleOwner, Observer { data ->

            productListAdapter.addData(data.products, viewModel.shouldAppend)

            if(data.pagingFilteringContext?.pageNumber ?: 1 == 1) {
                tvNoProduct.visibility =
                    if (data.products.isNullOrEmpty()) View.VISIBLE else View.GONE

                populateSortOptions(data.pagingFilteringContext)
                setToolbarTitle(data.name)
            }
        })

        viewModel.manufacturerLD.observe(viewLifecycleOwner, Observer { manufacturer ->
            productListAdapter.addData(manufacturer.products, viewModel.shouldAppend)

            if(manufacturer.pagingFilteringContext?.pageNumber ?: 1 == 1) {
                tvNoProduct.visibility =
                    if (manufacturer.products.isNullOrEmpty()) View.VISIBLE else View.GONE

                populateSortOptions(manufacturer.pagingFilteringContext)
                setToolbarTitle(manufacturer.name)
            }
        })

        viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })


        viewModel.filterVisibilityLD.observe(viewLifecycleOwner, Observer { show ->

            if(show) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                btnFilter.visibility = View.VISIBLE
                llButtonHolder.visibility = View.VISIBLE
            } else {
                // to turn off slide open drawer
                btnFilter.visibility = View.GONE
                llButtonHolder.visibility = View.INVISIBLE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        })

        viewModel.addedToWishListLD.observe(viewLifecycleOwner, Observer { action ->
            action?.getContentIfNotHandled()?.let { product ->
                replaceFragmentSafely(ProductDetailFragment.newInstance(
                    product.id?.toLong() ?: -1L, product.name))
            }
            blockingLoader.hideDialog()
        })
    }

    private fun setToolbarTitle(title: String?) {
        if(isAdded && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            requireActivity().title = title ?: ""
            arguments?.putString(CATEGORY_NAME, title ?: "")
        }
    }

    private fun initSubcategoryPopupWindow(catName: String?, subCatList: List<SubCategory>?) {

        subcategoryPlaceholder.visibility =
            if (subCatList.isNullOrEmpty()) View.GONE else View.VISIBLE

        if (subCatList.isNullOrEmpty()) {
            categoryNameTextView.visibility = View.GONE
            return
        }

        categoryNameTextView.visibility = View.VISIBLE
        categoryNameTextView.text = catName ?: ""

        subcategoryPopupWindow = ListPopupWindow(requireContext())

        subcategoryPopupWindow.anchorView = categoryNameTextView
        subcategoryPopupWindow.isModal = true
        subcategoryPopupWindow.setBackgroundDrawable(ColorDrawable(0))

        subcategoryPopupWindow.setAdapter(
            SubCategoryAdapter(
                subCatList
            )
        )

        categoryNameTextView?.setOnClickListener {
            if (subcategoryPopupWindow.isShowing) {
                subcategoryPopupWindow.dismiss()
            } else {
                subcategoryPopupWindow.show()
            }
        }

        subcategoryPopupWindow.setOnItemClickListener { parent, view, position, id ->
            subcategoryPopupWindow.dismiss()

            replaceFragmentSafely(newInstance(
                subCatList[position].name ?: "",
                subCatList[position].id ?: -1,
                GetBy.CATEGORY
            ))
        }

    }

    private fun calculateAutomaticGridColumn() {
        layoutManager = GridLayoutManager(requireContext(), 2)

        rvProductList?.layoutManager = layoutManager

        rvProductList?.viewTreeObserver?.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rvProductList?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                    val viewWidth = rvProductList?.measuredWidth ?: 720
                    val cardViewWidth =
                        requireContext().resources?.getDimension(R.dimen.product_item_size) ?: 330f

                    val newSpanCount = floor((viewWidth / cardViewWidth).toDouble()).toInt()

                    layoutManager.apply {
                        spanCount = newSpanCount
                        requestLayout()
                    }

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

                    rvProductList?.addItemDecoration(decoration)
                }
            })
    }

    fun applyFilter(filterUrl: String?) {

        val type = when (getBy) {
            GetBy.CATEGORY.name -> 1
            GetBy.MANUFACTURER.name -> 2
            GetBy.TAG.name -> 3
            else -> 4
        }

        (viewModel as ProductListViewModel).applyFilter(categoryId.toLong(),
            filterUrl, model, type)

        sortOptionDialog.hide()
        drawerLayout?.closeDrawers()

        // remove existing filters from view
        childFragmentManager
            .findFragmentById(R.id.filterFragmentHolder)
            ?.view
            ?.findViewById<LinearLayout>(R.id.attributeLayout)
            ?.removeAllViews()
    }


    enum class GetBy {
        CATEGORY, MANUFACTURER, TAG, VENDOR
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