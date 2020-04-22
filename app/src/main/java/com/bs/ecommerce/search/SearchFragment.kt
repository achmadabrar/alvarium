package com.bs.ecommerce.search

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.ProductListAdapter
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.ProductListModelImpl
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.inflate
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlin.math.floor

class SearchFragment : BaseFragment() {

    private lateinit var model: ProductListModel
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productClickListener: ItemClickListener<ProductSummary>

    private var viewCreated = false
    private var rootView: View? = null



    private var searchView: SearchView? = null

    private var productAdapter: ProductListAdapter? = null


    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getRootLayout(): RelativeLayout = productListRootLayout

    override fun createViewModel(): BaseViewModel = SearchViewModel()

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

        setHasOptionsMenu(true)

        if(!viewCreated) {

            calculateAutomaticGridColumn()

            model = ProductListModelImpl()
            viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)


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
        val viewModel = viewModel as SearchViewModel

        viewModel.productLiveData.observe(viewLifecycleOwner, Observer { data ->

            rvProductList.adapter = ProductListAdapter(data.products!!, productClickListener)


            llButtonHolder.visibility =
                if (data.subCategories.isNullOrEmpty()) View.VISIBLE else View.GONE
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

            if (productAdapter == null)
                searchView?.queryHint = getString(R.string.title_search)
            else
                searchView?.queryHint = MyApplication.searchQuery

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchProduct()
                    activity?.title = query
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
                    fragmentManager?.popBackStack()
                    return true
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun searchProduct()
    {

        searchView?.let {

            val query = it.query.toString()

            if (query.length > 2)
            {
                //(viewModel as ProductListViewModel).getProductByCategory(categoryId.toLong(), model)
                //RetroClient.api.searchProduct(searchObject, queryMapping).enqueue(CustomCB(searchRootLayout))
            }
            else
                toast(R.string.search_limit)

        }
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