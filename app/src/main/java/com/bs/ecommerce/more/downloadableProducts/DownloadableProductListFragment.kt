package com.bs.ecommerce.more.downloadableProducts

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
import com.bs.ecommerce.more.OrderDetailsFragment
import com.bs.ecommerce.more.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.more.downloadableProducts.model.DownloadableProductListModelImpl
import com.bs.ecommerce.more.downloadableProducts.model.data.Item
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_customer_address.tvNoData
import kotlinx.android.synthetic.main.fragment_customer_downloadable_product_list.*

class DownloadableProductListFragment : BaseFragment() {

    private lateinit var model: DownloadableProductListModel
    private lateinit var listAdapter: DownloadableProductListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_customer_downloadable_product_list

    override fun getRootLayout(): RelativeLayout? = downloadableProductListRootLayout

    override fun createViewModel(): BaseViewModel = DownloadableProductListViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_DOWNLOADABLE_PRODUCTS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            model = DownloadableProductListModelImpl()
            viewModel = ViewModelProvider(this).get(DownloadableProductListViewModel::class.java)

            setupView()

            (viewModel as DownloadableProductListViewModel).getProductList(model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as DownloadableProductListViewModel).apply {

            productListLD.observe(viewLifecycleOwner, Observer { data ->

                tvNoData.visibility = if (data?.items.isNullOrEmpty())
                    View.VISIBLE else View.GONE

                listAdapter.addData(data?.items)
            })
            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })
        }
    }

    private fun addFragment(fragment: androidx.fragment.app.Fragment)
    {
        requireActivity().supportFragmentManager
            .beginTransaction().add(R.id.layoutFrame, fragment).addToBackStack(null).commit()
    }


    private fun setupView() {


        val clickListener = object : ItemClickListener<Item> {
            override fun onClick(view: View, position: Int, data: Item) {

                when (view.id) {

                    R.id.icDownload -> {    }

                    R.id.tv1 -> {
                        addFragment(ProductDetailFragment.newInstance(productId = data.productId.toLong(), productName = data.productName))
                    }

                    R.id.tv2 -> {
                        addFragment(OrderDetailsFragment.newInstance(orderId = data.orderId))
                    }
                }
            }
        }

        rvDownloadableProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))

            listAdapter = DownloadableProductListAdapter(clickListener, requireActivity())
            adapter = listAdapter
        }
    }
}