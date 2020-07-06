package com.bs.ecommerce.more.vendor

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.wishlist.WishListViewModel
import com.bs.ecommerce.catalog.productList.ProductListFragment
import com.bs.ecommerce.more.vendor.model.VendorModel
import com.bs.ecommerce.more.vendor.model.VendorModelImpl
import com.bs.ecommerce.catalog.common.ProductByVendorData
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_vendor_list.*

class VendorListFragment : BaseFragment() {
    private lateinit var model: VendorModel
    private lateinit var listAdapter: VendorAdapter

    override fun getLayoutId(): Int = R.layout.fragment_vendor_list

    override fun getRootLayout(): RelativeLayout? = wishListRootLayout

    override fun createViewModel(): BaseViewModel =
        WishListViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.PRODUCT_VENDOR)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = VendorModelImpl()
            viewModel = ViewModelProvider(this).get(VendorViewModel::class.java)

            (viewModel as VendorViewModel).getAllVendors(model)

            initView()
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {

        (viewModel as VendorViewModel).apply {

            vendorLD.observe(viewLifecycleOwner, Observer { data ->

                tvNoData.visibility = if (data.isEmpty())
                    View.VISIBLE else View.GONE

                listAdapter.addData(data)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    private fun initView() {

        tvNoData.text = DbHelper.getString(Const.COMMON_NO_DATA)

        val clickListener = object : ItemClickListener<ProductByVendorData> {

            override fun onClick(view: View, position: Int, data: ProductByVendorData) {

                when (view.id) {

                    R.id.btnContactVendor ->
                        if (data.id != null)
                            replaceFragmentSafely(
                                ContactVendorFragment.newInstance(
                                    data.id,
                                    data.name
                                )
                            )

                    R.id.itemView ->
                        if (data.id != null)
                            replaceFragmentSafely(
                                ProductListFragment.newInstance(
                                    data.name ?: "",
                                    data.id,
                                    ProductListFragment.GetBy.VENDOR
                                )
                            )
                }
            }
        }

        rvList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))

            listAdapter =
                VendorAdapter(clickListener)
            adapter = listAdapter
        }

    }

}