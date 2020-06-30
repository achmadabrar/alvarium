package com.bs.ecommerce.home.manufaturer

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.options.model.CommonModelImpl
import com.bs.ecommerce.catalog.productList.ProductListFragment
import com.bs.ecommerce.home.homepage.model.data.Manufacturer
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.calculateColumnsForGridLayout
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_manufacturer_list.*

class ManufacturerListFragment : BaseFragment() {

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_MANUFACTURER)

    override fun getLayoutId(): Int = R.layout.fragment_manufacturer_list

    override fun getRootLayout(): RelativeLayout? = productReviewRootLayout

    override fun createViewModel(): BaseViewModel = ManufacturerViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            viewModel = ViewModelProvider(this).get(ManufacturerViewModel::class.java)

            (viewModel as ManufacturerViewModel).getAllManufacturers(CommonModelImpl())

            setupView()
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as ManufacturerViewModel).apply {

            manufacturerLD.observe(viewLifecycleOwner, Observer { list ->

                tvNoData.visibility = if (list.isNullOrEmpty())
                    View.VISIBLE else View.GONE

                populateListView(list)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                    if (isShowLoader)
                        showLoading()
                    else
                        hideLoading()
                })
        }
    }

    private fun populateListView(list: List<Manufacturer>?) {
        rvList?.adapter =
            ManufacturerListAdapter(
                list ?: listOf(),
                object : ItemClickListener<Manufacturer> {
                    override fun onClick(view: View, position: Int, data: Manufacturer) {

                        replaceFragmentSafely(
                            ProductListFragment.newInstance(
                                data.name ?: "",
                                data.id ?: -1,
                                ProductListFragment.GetBy.MANUFACTURER
                            )
                        )
                    }
                })
    }

    private fun setupView() {
        tvNoData?.text = DbHelper.getString(Const.COMMON_NO_DATA)
        rvList?.calculateColumnsForGridLayout()
    }
}