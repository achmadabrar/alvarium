package com.bs.ecommerce.product

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_product_by_category.*

class ProductByCategoryFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_product_by_category

    override fun getRootLayout(): RelativeLayout = prodByCategoryRootLayout

    override fun createViewModel(): BaseViewModel = ProductDetailViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }

    companion object {
        @JvmStatic
        private val CATEGORY_NAME = "categoryName"
        @JvmStatic
        private val CATEGORY_ID = "categoryId"

        @JvmStatic
        fun newInstance(categoryName: String, categoryId: Int): ProductByCategoryFragment {
            val fragment = ProductByCategoryFragment()
            val args = Bundle()
            args.putString(CATEGORY_NAME, categoryName)
            args.putInt(CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
    }
}