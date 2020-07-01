package com.bs.ecommerce.account.downloadableProducts

import android.content.Intent
import android.net.Uri
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
import com.bs.ecommerce.account.orders.OrderDetailsFragment
import com.bs.ecommerce.account.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.account.downloadableProducts.model.DownloadableProductListModelImpl
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductItem
import com.bs.ecommerce.catalog.product.ProductDetailFragment
import com.bs.ecommerce.account.downloadableProducts.model.data.UserAgreementData
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_customer_address.tvNoData
import kotlinx.android.synthetic.main.fragment_customer_downloadable_product_list.*
import kotlinx.android.synthetic.main.item_downloadable_product.view.*

class DownloadableProductListFragment : BaseFragment() {

    private lateinit var model: DownloadableProductListModel
    private lateinit var listAdapter: DownloadableProductListAdapter

    private var currentGuid: String = ""

    override fun getLayoutId(): Int = R.layout.fragment_customer_downloadable_product_list

    override fun getRootLayout(): RelativeLayout? = downloadableProductListRootLayout

    override fun createViewModel(): BaseViewModel =
        DownloadableProductListViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_DOWNLOADABLE_PRODUCTS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            model =
                DownloadableProductListModelImpl()
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

            isLoadingLD.observe(viewLifecycleOwner, Observer {
                    isShowLoader -> showHideLoader(isShowLoader)
            })

            productDownloadLD.observe(viewLifecycleOwner,
                Observer { sampleDownloadResp ->
                    blockingLoader.hideDialog()

                    sampleDownloadResp?.let {
                        it.getContentIfNotHandled()?.let { data ->
                            data.data?.apply {
                                // open browser
                                try {
                                    if(this.hasUserAgreement == true) {

                                        // Call user agreement api
                                        getUserAgreementData(currentGuid, model)

                                    } else if (this.downloadUrl != null ){
                                        val browserIntent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(this.downloadUrl))
                                        startActivity(browserIntent)
                                    }
                                } catch (e: Exception) {
                                    e.localizedMessage?.let { msg -> toast(msg) }
                                }
                            }
                        }
                    }
                })

            userAgreementLD.observe(viewLifecycleOwner, Observer {

                it.getContentIfNotHandled()?.let { agreementData ->
                    UserAgreementDialogFragment.newInstance(agreementData).show(
                        requireActivity().supportFragmentManager, "Dialog"
                    )
                }
            })

        }
    }

    private fun addFragment(fragment: androidx.fragment.app.Fragment)
    {
        requireActivity().supportFragmentManager
            .beginTransaction().add(R.id.layoutFrame, fragment).addToBackStack(null).commit()
    }
    
    fun onUserAgreeClicked(data: UserAgreementData) {
        blockingLoader.showDialog()

        (viewModel as DownloadableProductListViewModel).downloadProduct(
            data.orderItemGuid ?: "", "true", model
        )
    }


    private fun setupView() {


        val clickListener = object : ItemClickListener<DownloadableProductItem> {
            override fun onClick(view: View, position: Int, data: DownloadableProductItem) {

                when (view.id) {

                    R.id.icDownload -> {
                        currentGuid = data.orderItemGuid ?: ""
                        blockingLoader.showDialog()

                        (viewModel as DownloadableProductListViewModel).downloadProduct(
                           currentGuid, "null", model
                        )
                    }

                    R.id.textView1 -> {
                        addFragment(ProductDetailFragment.newInstance(productId = data.productId.toLong(), productName = data.productName))
                    }

                    R.id.textView2 -> {
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

            listAdapter =
                DownloadableProductListAdapter(
                    clickListener,
                    requireActivity()
                )
            adapter = listAdapter
        }
    }
}