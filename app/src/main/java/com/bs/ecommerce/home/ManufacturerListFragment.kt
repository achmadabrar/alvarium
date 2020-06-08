package com.bs.ecommerce.home

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.product.ProductListFragment
import com.bs.ecommerce.product.model.data.Manufacturer
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.calculateColumnsForGridLayout
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_manufacturer_list.*

class ManufacturerListFragment : BaseFragment() {

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_MANUFACTURER)

    override fun getLayoutId(): Int = R.layout.fragment_manufacturer_list

    override fun getRootLayout(): RelativeLayout? = productReviewRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            setupView()
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as MainViewModel).manufacturerListLD.observe(viewLifecycleOwner, Observer { list ->

            rvList?.adapter =
                ManufacturerListAdapter(list, object : ItemClickListener<Manufacturer> {
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
        })
    }

    private fun setupView() {
        rvList.calculateColumnsForGridLayout()
    }
}