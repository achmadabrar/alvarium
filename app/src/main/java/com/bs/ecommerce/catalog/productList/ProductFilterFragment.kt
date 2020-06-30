package com.bs.ecommerce.catalog.productList

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.catalog.common.FilterItems
import com.bs.ecommerce.catalog.common.PriceRangeFilter
import com.bs.ecommerce.catalog.search.SearchFragment
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.fragment_product_filter.*
import kotlinx.android.synthetic.main.price_filter_option.view.*
import kotlinx.android.synthetic.main.product_applied_filter.view.*
import kotlinx.android.synthetic.main.product_filter_option.view.tvTitle
import kotlinx.android.synthetic.main.product_filter_option_for_color.view.*

class ProductFilterFragment : BaseFragment() {
    private val logTag = "nop_" + this::class.java.simpleName
    private var viewGroup: ViewGroup? = null

    override fun getLayoutId(): Int = R.layout.fragment_product_filter

    override fun getRootLayout(): RelativeLayout? = productFilterRootLayout

    override fun createViewModel() =
        ProductListViewModel()

    override fun getFragmentTitle() = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Explicitly used deprecated library to access viewModel with this parent fragments scope
        viewModel = ViewModelProviders.of(parentFragment!!).get(ProductListViewModel::class.java)

        attributeLayout?.removeAllViews()

        setLiveDataListener()
    }

    private fun setLiveDataListener() {
        (viewModel as ProductListViewModel).apply {

            priceRangeLD.observe(viewLifecycleOwner, Observer { priceRangeFilter ->
                Log.d(logTag, "priceRange size ${priceRangeFilter?.items?.size}")

                populatePriceFilter(priceRangeFilter)
            })

            applicableFilterLD.observe(viewLifecycleOwner, Observer { mMap ->
                Log.d(logTag, "applicable filter size ${mMap.size}")

                populateItemSpecificFilters(mMap)
            })

            appliedFilterLD.observe(viewLifecycleOwner, Observer { mMap ->
                Log.d(logTag, "applied filter size ${mMap.size}")

                populateAlreadyAppliedFilters(mMap)
            })
        }
    }

    private fun populatePriceFilter(priceRangeFilter: PriceRangeFilter?) {

        val priceRange = priceRangeFilter?.items ?: listOf()

        if (priceRange.isNotEmpty()) {

            val view = layoutInflater.inflate(R.layout.product_filter_option, viewGroup) as LinearLayout
            view.tvTitle.text = DbHelper.getString(Const.FILTER_PRICE_RANGE)

            for (i in priceRange) {
                val rl = layoutInflater.inflate(
                    R.layout.price_filter_option,
                    view,
                    false
                ) as RelativeLayout

                rl.tvName.text = i.toString()
                rl.btnRemoveFilter.visibility = if (i.selected) View.VISIBLE else View.GONE

                rl.btnRemoveFilter.text = DbHelper.getString(Const.FILTER_REMOVE)
                rl.btnRemoveFilter.setOnClickListener {
                    if (parentFragment is ProductListFragment && i.selected)
                        (parentFragment as ProductListFragment).applyFilter(priceRangeFilter?.removeFilterUrl)

                    else if (parentFragment is SearchFragment && i.selected)
                        (parentFragment as SearchFragment).applyFilter(priceRangeFilter?.removeFilterUrl)
                }

                rl.setOnClickListener {
                    if (parentFragment is ProductListFragment && !i.selected)
                        (parentFragment as ProductListFragment).applyFilter(i.filterUrl)

                    else if (parentFragment is SearchFragment && i.selected)
                        (parentFragment as SearchFragment).applyFilter(priceRangeFilter?.removeFilterUrl)
                }

                view.addView(rl)
            }

            attributeLayout.addView(view)
        }
    }

    private fun populateItemSpecificFilters(mMap: MutableMap<String, MutableList<FilterItems>>) {
        for (tmp in mMap) {
            if (tmp.key == "Color") {
                attributeLayout.addView(color(tmp))
            } else {
                attributeLayout.addView(allExColor(tmp))
            }
        }
    }

    private fun populateAlreadyAppliedFilters(mMap: MutableMap<String, MutableList<FilterItems>>) {

        if(mMap.isNullOrEmpty()) return

        val view = layoutInflater.inflate(R.layout.product_applied_filter, viewGroup) as LinearLayout
        view.tvCurrentlyShoppingBy.text = DbHelper.getString(Const.FILTER_FILTER_BY)

        val title = StringBuilder()
        var clearFilterUrl: String? = null

        for (i in mMap) {

            title.append(i.key).append(" :\n")

            for(j in i.value) {
                title.append((j.specificationAttributeOptionName)).append("\n")
                clearFilterUrl = j.filterUrl
            }

            title.append("\n")
        }

        view.tvAppliedAttributes.text = title.toString().trimEnd()

        view.btnClearFilter.text = DbHelper.getString(Const.FILTER_REMOVE)
        view.btnClearFilter.setOnClickListener {
            if (parentFragment is ProductListFragment)
                (parentFragment as ProductListFragment).applyFilter(clearFilterUrl)
        }

        attributeLayout.addView(view)
    }

    private fun color(tmp: Map.Entry<String, MutableList<FilterItems>>): View {
        val view =
            layoutInflater.inflate(R.layout.product_filter_option_for_color, viewGroup) as LinearLayout
        view.tvTitle.text = DbHelper.getString(Const.FILTER_SPECIFICATION).plus(": ${tmp.key}")

        for (i in tmp.value) {
            val rb = layoutInflater.inflate(
                R.layout.radiobutton_product_color,
                view.radioGridGroup,
                false
            ) as AppCompatRadioButton
            rb.isChecked = false

            ViewCompat.setBackgroundTintList(
                rb,
                ColorStateList.valueOf(
                    Color.parseColor(
                        i.specificationAttributeOptionColorRgb ?: "#000"
                    )
                )
            )

            rb.setOnClickListener {
                if (parentFragment is ProductListFragment)
                    (parentFragment as ProductListFragment).applyFilter(i.filterUrl)
            }

            view.radioGridGroup.addView(rb)
        }

        return view
    }

    private fun allExColor(tmp: Map.Entry<String, MutableList<FilterItems>>): View {
        val view = layoutInflater.inflate(R.layout.product_filter_option, viewGroup) as LinearLayout
        view.tvTitle.text = DbHelper.getString(Const.FILTER_SPECIFICATION).plus(": ${tmp.key}")

        for (i in tmp.value) {

            val tv = layoutInflater.inflate(R.layout.custom_attribute_dropdown, view, false) as TextView
            tv.text = i.specificationAttributeOptionName
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            tv.setOnClickListener {
                if (parentFragment is ProductListFragment)
                    (parentFragment as ProductListFragment).applyFilter(i.filterUrl)
            }
            view.addView(tv)
        }

        return view
    }

    /*companion object {
        @JvmStatic
        private val FILTER = "filter"

        @JvmStatic
        fun newInstance(filterInfo: PagingFilteringContext): ProductFilterFragment {
            val fragment = ProductFilterFragment()
            val args = Bundle()
            args.putParcelable(FILTER, filterInfo)
            fragment.arguments = args
            return fragment
        }
    }*/
}