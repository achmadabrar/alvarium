package com.bs.ecommerce.product

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.product.adapter.ProductListAdapter
import com.bs.ecommerce.product.model.SearchModel
import com.bs.ecommerce.product.model.SearchModelImpl
import com.bs.ecommerce.product.model.data.AdvancedSearch
import com.bs.ecommerce.product.model.data.AvailableCategory
import com.bs.ecommerce.product.model.data.PagingFilteringContext
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.viewModel.ProductListViewModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.advanced_search_layout.*
import kotlinx.android.synthetic.main.fragment_product_list.*
import java.util.*
import kotlin.math.floor

class SearchFragment : BaseFragment() {

    private var searchProductItemClicked = false
    private var previousSearchQuery = ""

    private lateinit var model: SearchModel
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productClickListener: ItemClickListener<ProductSummary>
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    private lateinit var productListAdapter: ProductListAdapter
    private var searchView: SearchView? = null
    private val sortOptionDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(requireContext(), R.style.BsDialog)
    }

    private var observeLiveDataChange = true

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_NAV_SEARCH)

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel = ProductListViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        if(!viewCreated) {

            model = SearchModelImpl()
            viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
            observeLiveDataChange = true

            initView()

        } else {
            observeLiveDataChange = false
            (viewModel as ProductListViewModel).shouldAppend = false
        }

        setLiveDataListeners()

    }

    private fun initView()
    {
        initAdvancedSearch()

        calculateAutomaticGridColumn()

        tvNoProduct.text = DbHelper.getString(Const.SEARCH_NO_RESULT)

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

                replaceFragmentSafely(ProductDetailFragment.newInstance(data.id.toLong(), data.name))
            }
        }

        productListAdapter = ProductListAdapter(
            productClickListener
        )

        rvProductList.adapter = productListAdapter

        rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible == totalItemCount-1
                if (totalItemCount > 0 && endHasBeenReached) {
                    Log.d("nop_", "last item of Product list is visible")

                    observeLiveDataChange = true
                    (viewModel as ProductListViewModel).searchProduct(advancedSearchData, false, model)
                }
            }
        })

        // drawer view
        childFragmentManager
            .beginTransaction()
            .replace(R.id.filterFragmentHolder, ProductFilterFragment())
            .commit()
    }

    private fun setLiveDataListeners()
    {
        with(viewModel as ProductListViewModel)
        {
            searchResultLD.observe(viewLifecycleOwner, Observer { searchResult ->

                if(observeLiveDataChange) productListAdapter.addData(searchResult.products, shouldAppend)

                llButtonHolder.visibility =
                    if (searchResult.noResults == false) View.VISIBLE else View.GONE
                tvNoProduct.visibility =
                    if (searchResult.noResults == true && !shouldAppend) View.VISIBLE else View.GONE

                populateSortOptions(searchResult.pagingFilteringContext)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })

            filterVisibilityLD.observe(viewLifecycleOwner, Observer { show ->

                if(show) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    btnFilter.visibility = View.VISIBLE
                } else {
                    // to turn off slide open drawer
                    btnFilter.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            })

            availableCategoriesLD.observe(viewLifecycleOwner, Observer { availableCategoryList ->

                if (availableCategoryList.isNotEmpty())
                {
                    categoryList.clear()
                    categoryList.addAll(availableCategoryList)

                    categorySpinner?.adapter = createSpinnerAdapter(categoryList.map { it.text } as List<String>)
                }
            })
            availableManufacturersLD.observe(viewLifecycleOwner, Observer { availableManufacturerList ->

                if (availableManufacturerList.isNotEmpty())
                {
                    manufacturerList.clear()
                    manufacturerList.addAll(availableManufacturerList)

                    manufacturerSpinner?.adapter =
                        createSpinnerAdapter(manufacturerList.map { it.text } as List<String>)
                }

            })
            availableVendorsLD.observe(viewLifecycleOwner, Observer { availableVendorList ->

                if (availableVendorList.isNotEmpty())
                {
                    vendorList.clear()
                    vendorList.addAll(availableVendorList)

                    vendorSpinner?.adapter =
                        createSpinnerAdapter(vendorList.map { it.text } as List<String>)
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchProductItemClicked = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.menu_product_search, menu)

        val item = menu.findItem(R.id.action_search)

        try {
            searchView = SearchView((activity as MainActivity).supportActionBar?.themedContext)

            MenuItemCompat.setShowAsAction(
                item,
                MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItemCompat.SHOW_AS_ACTION_ALWAYS)
            MenuItemCompat.setActionView(item, searchView)

            if (searchProductItemClicked)
            {
                searchView?.queryHint = previousSearchQuery
                Handler().post {  requireActivity().hideKeyboard()  }
            }
            else
            {
                searchView?.queryHint = DbHelper.getString(Const.TITLE_SEARCH)
                searchProductItemClicked = true
            }


            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchProduct()

                    requireActivity().title = query
                    previousSearchQuery = query
                    searchView?.clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })

            searchView?.isIconified = false
            item?.expandActionView()


            item?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean = true

                override fun onMenuItemActionCollapse(menuItem: MenuItem) : Boolean
                {
                    searchView?.clearFocus()
                    requireActivity().supportFragmentManager.popBackStack()
                    return true
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private lateinit var query:String

    private var categoryList: MutableList<AvailableCategory> = ArrayList()
    private var manufacturerList: MutableList<AvailableCategory> = ArrayList()
    private var vendorList: MutableList<AvailableCategory> = ArrayList()

    private val advancedSearchData: AdvancedSearch
        get()
        {
            val search = AdvancedSearch()

            searchView?.let {   search.query = it.query.toString()  }

            if (advanceSearchCheckBox?.isChecked!!)
            {
                search.isAdvanceSearchSelected = true

                search.isSearchInSubcategory = searchInSubCategory?.isChecked!!

                search.categoryId = categorySpinner?.selectedItemId?.toInt() ?: 0
                search.manufacturerId = manufacturerSpinner?.selectedItemId?.toInt() ?: 0
                search.vendorId = vendorSpinner?.selectedItemId?.toInt() ?: 0

                search.isSearchVendor = true

                search.priceFrom = priceFromEditText!!.text.toString().trim { it <= ' ' }
                search.priceTo = priceToEditText!!.text.toString().trim { it <= ' ' }

                search.isSearchInDescription = searchInDescription?.isChecked!!
            }
            return search
        }


    fun searchProduct() {

        searchView?.let {

            query = it.query.toString()

            if (query.length > 2) {
                observeLiveDataChange = true
                (viewModel as ProductListViewModel).searchProduct(advancedSearchData, true, model)
            } else
                toast(DbHelper.getString(Const.SEARCH_QUERY_LENGTH))

        }
    }

    private fun populateSortOptions(sortOption: PagingFilteringContext?) {

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

    fun applyFilter(filterUrl: String?) {
        observeLiveDataChange = true
        (viewModel as ProductListViewModel).applySearchFilter(filterUrl, model)

        sortOptionDialog.hide()
        drawerLayout?.closeDrawers()

        // remove existing filters from view
        childFragmentManager
            .findFragmentById(R.id.filterFragmentHolder)
            ?.view
            ?.findViewById<LinearLayout>(R.id.attributeLayout)
            ?.removeAllViews()
    }

    private fun calculateAutomaticGridColumn() {
        layoutManager = GridLayoutManager(requireContext(), 2)

        rvProductList?.layoutManager = layoutManager

        rvProductList?.viewTreeObserver?.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rvProductList?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

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

    private fun setDynamicStrings()
    {
        advanceSearchCheckBox?.text = DbHelper.getString(Const.ADVANCED_SEARCH)

        label_category?.text = DbHelper.getString(Const.HOME_NAV_CATEGORY)
        searchInSubCategory?.text = DbHelper.getString(Const.AUTOMATICALLY_SEARCH_SUBCATEGORIES)

        label_manufacturer?.text = DbHelper.getString(Const.PRODUCT_MANUFACTURER)
        label_vendor?.text = DbHelper.getString(Const.PRODUCT_VENDOR)

        label_price_range_from?.text = DbHelper.getString(Const.PRICE_RANGE)
        label_price_range_to?.text = DbHelper.getString(Const.TO)

        searchInDescription?.text = DbHelper.getString(Const.SEARCH_IN_PRODUCT_DISCRIPTIONS)

        searchButton?.text = DbHelper.getString(Const.HOME_NAV_SEARCH)
    }

    private fun initAdvancedSearch()
    {
        bsBehavior = BottomSheetBehavior.from(advanceSearchFullView)

        setDynamicStrings()

        advanceSearchFullView?.visibility = View.VISIBLE

        initSearchCategorySpinner()
        initSearchManufacturerSpinner()
        initSearchVendorSpinner()

        advanceSearchCheckBox?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                //advanceSearchLayout?.visibility = View.VISIBLE
            else
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                //advanceSearchLayout?.visibility = View.GONE
        }
        searchButton?.setOnClickListener {
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            searchProduct()
        }

        (viewModel as ProductListViewModel).getModelsForAdvancedSearch(model)
    }

    private fun createSpinnerAdapter(nameList: List<String>): ArrayAdapter<String>
    {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, nameList)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        return dataAdapter
    }

    private fun initSearchCategorySpinner()
    {
        categoryList.add(AvailableCategory(false,null,false, DbHelper.getString(Const.SEARCH_ALL),"0"))
        categorySpinner?.adapter = createSpinnerAdapter(categoryList.map { it.text } as List<String>)
    }

    private fun initSearchManufacturerSpinner()
    {
        manufacturerList.add(AvailableCategory(false,null,false, DbHelper.getString(Const.SEARCH_ALL),"0"))
        manufacturerSpinner?.adapter = createSpinnerAdapter(manufacturerList.map { it.text } as List<String>)
    }
    private fun initSearchVendorSpinner()
    {
        vendorList.add(AvailableCategory(false,null,false, DbHelper.getString(Const.SEARCH_ALL),"0"))
        vendorSpinner?.adapter = createSpinnerAdapter(vendorList.map { it.text } as List<String>)
    }

}