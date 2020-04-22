package com.bs.ecommerce.product

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.product.model.data.FilterItems
import com.bs.ecommerce.product.model.data.PriceRange
import com.bs.ecommerce.product.viewModel.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_product_filter.*
import kotlinx.android.synthetic.main.product_filter_option.view.tvTitle
import kotlinx.android.synthetic.main.product_filter_option_for_color.view.*

class ProductFilterFragment : BaseFragment() {
    private val logTag = "nop_" + this::class.java.simpleName

    override fun getLayoutId(): Int = R.layout.fragment_product_filter

    override fun getRootLayout(): RelativeLayout? = productFilterRootLayout

    override fun createViewModel() = ProductListViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Explicitly used deprecated library to access viewModel with this parent fragments scope
        viewModel = ViewModelProviders.of(parentFragment!!).get(ProductListViewModel::class.java)

        attributeLayout?.removeAllViews()

        setLiveDataListener()
    }

    private fun setLiveDataListener() {
        (viewModel as ProductListViewModel).apply {

            priceRangeLD.observe(viewLifecycleOwner, Observer { priceRange ->
                Log.d(logTag, "priceRange size ${priceRange.size}")

                populatePriceFilterOptions(priceRange)
            })

            filterAttributeLD.observe(viewLifecycleOwner, Observer { mMap ->
                Log.d(logTag, "map size ${mMap.size}")

                populateItemSpecificOptions(mMap)
            })
        }
    }

    private fun populatePriceFilterOptions(priceRange: List<PriceRange>) {

        if (priceRange.isNotEmpty()) {

            val view = layoutInflater.inflate(R.layout.product_filter_option, null) as LinearLayout
            view.tvTitle.text = getString(R.string.filter_by_price)

            for (i in priceRange) {
                val tv = layoutInflater.inflate(R.layout.generic_attr_item, view, false) as TextView
                tv.text = i.toString()
                tv.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    if (i.selected) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                    0
                )

                tv.setOnClickListener {
                    if (parentFragment is ProductListFragment)
                        (parentFragment as ProductListFragment).applyFilter(i.filterUrl)
                }

                view.addView(tv)
            }

            attributeLayout.addView(view)
        }
    }

    private fun populateItemSpecificOptions(mMap: MutableMap<String, MutableList<FilterItems>>) {
        for (tmp in mMap) {
            if (tmp.key == "Color") {
                attributeLayout.addView(color(tmp))
            } else {
                attributeLayout.addView(allExColor(tmp))
            }
        }
    }

    private fun color(tmp: Map.Entry<String, MutableList<FilterItems>>): View {
        val view =
            layoutInflater.inflate(R.layout.product_filter_option_for_color, null) as LinearLayout
        view.tvTitle.text = "Filter By ${tmp.key}"

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
        val view = layoutInflater.inflate(R.layout.product_filter_option, null) as LinearLayout
        view.tvTitle.text = getString(R.string.filter_by_attr, tmp.key)

        for (i in tmp.value) {

            val tv = layoutInflater.inflate(R.layout.generic_attr_item, view, false) as TextView
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