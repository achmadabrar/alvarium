package com.bs.ecommerce.product

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
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
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.product.adapter.ProductListAdapter
import com.bs.ecommerce.product.model.SearchModel
import com.bs.ecommerce.product.model.SearchModelImpl
import com.bs.ecommerce.product.model.data.PagingFilteringContext
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.viewModel.ProductListViewModel
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.sort_option_bottom_sheet.view.*
import kotlin.math.floor

class SearchFragment : BaseFragment() {

    private lateinit var model: SearchModel
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productClickListener: ItemClickListener<ProductSummary>

    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    private var searchView: SearchView? = null

    override fun getFragmentTitle() = R.string.title_search

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel = ProductListViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        if(!viewCreated) {

            initView()

            model = SearchModelImpl()
            viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)

        } else {
            (viewModel as ProductListViewModel).shouldAppend = false
        }

        setLiveDataListeners()

    }

    private fun initView() {
        calculateAutomaticGridColumn()

        btnFilter.setOnClickListener {
            when (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                true -> {
                    drawerLayout?.closeDrawers()
                }
                false -> {
                    bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    drawerLayout?.openDrawer(GravityCompat.END)
                }
            }
        }

        btnBrand.setOnClickListener {
            if(bsBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                drawerLayout?.closeDrawers()
                bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        productClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {

                if (data.id == null) return

                replaceFragmentSafely(ProductDetailFragment.newInstance(data.id.toLong(), data.name))
            }
        }

        bsBehavior = BottomSheetBehavior.from(bottomSheetLayout)

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

                    (viewModel as ProductListViewModel).searchProduct(query, false, model)
                }
            }
        })

        // drawer view
        childFragmentManager
            .beginTransaction()
            .replace(R.id.filterFragmentHolder, ProductFilterFragment())
            .commit()
    }

    private fun setLiveDataListeners() {
        val viewModel = viewModel as ProductListViewModel

        viewModel.searchResultLD.observe(viewLifecycleOwner, Observer { searchResult ->

            productListAdapter.addData(searchResult.products, viewModel.shouldAppend)

            llButtonHolder.visibility =
                if (searchResult.noResults == false) View.VISIBLE else View.GONE
            tvNoProduct.visibility =
                if (searchResult.noResults == true && !viewModel.shouldAppend) View.VISIBLE else View.GONE

            populateSortOptions(searchResult.pagingFilteringContext)
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

        viewModel.filterVisibilityLD.observe(viewLifecycleOwner, Observer { show ->

            if(show) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                btnFilter.visibility = View.VISIBLE
            } else {
                // to turn off slide open drawer
                btnFilter.visibility = View.GONE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        })
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

            searchView?.queryHint = getString(R.string.title_search)
            /*if (productListAdapter == null)
                searchView?.queryHint = getString(R.string.title_search)
            else
                searchView?.queryHint = MyApplication.searchQuery*/

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchProduct()

                    requireActivity().title = query
                    MyApplication.searchQuery = query
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

    fun searchProduct() {

        searchView?.let {

            query = it.query.toString()

            if (query.length > 2) {
                (viewModel as ProductListViewModel).searchProduct(query, true, model)
            } else
                toast(R.string.search_limit)

        }
    }

    private fun populateSortOptions(sortOption: PagingFilteringContext?) {

        bottomSheetLayout.sortOptionHolder.removeAllViews()

        if(sortOption?.allowProductSorting == true && !sortOption.availableSortOptions.isNullOrEmpty()) {

            for(i in sortOption.availableSortOptions) {

                val tv = layoutInflater.inflate(R.layout.custom_attribute_dropdown, null) as TextView
                tv.text = i.text
                tv.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    if (i.selected == true) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                    0
                )
                bottomSheetLayout.sortOptionHolder.addView(tv)

                tv.setOnClickListener {
                    applyFilter(i.value)
                }
            }
        } else {
            btnBrand.visibility = View.GONE
            btn_vertical_divider.visibility = View.GONE
        }
    }

    fun applyFilter(filterUrl: String?) {
        (viewModel as ProductListViewModel).applySearchFilter(filterUrl, model)

        bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
}