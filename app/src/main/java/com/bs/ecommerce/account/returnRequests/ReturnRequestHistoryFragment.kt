package com.bs.ecommerce.account.returnRequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.account.returnRequests.model.ReturnRequestModel
import com.bs.ecommerce.account.returnRequests.model.ReturnRequestModelImpl
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryItem
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.catalog.product.ProductDetailFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.fragment_customer_address.tvNoData
import kotlinx.android.synthetic.main.fragment_customer_downloadable_product_list.*
import kotlinx.android.synthetic.main.item_downloadable_product.view.*
import java.lang.ref.WeakReference

class ReturnRequestHistoryFragment : BaseFragment() {

    private lateinit var model: ReturnRequestModel
    private lateinit var listAdapter: ReturnRequestHistoryAdapter


    override fun getLayoutId(): Int = R.layout.fragment_customer_downloadable_product_list

    override fun getRootLayout(): RelativeLayout? = downloadableProductListRootLayout

    override fun createViewModel(): BaseViewModel =
        ReturnRequestHistoryViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_RETURN_REQUESTS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            model =
                ReturnRequestModelImpl()
            viewModel = ViewModelProvider(this).get(ReturnRequestHistoryViewModel::class.java)

            setupView()

            (viewModel as ReturnRequestHistoryViewModel).getReturnRequestHistory(model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as ReturnRequestHistoryViewModel).apply {

            returnRequestHistoryLD.observe(viewLifecycleOwner, Observer { data ->

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

                })

        }
    }

    private fun addFragment(fragment: androidx.fragment.app.Fragment)
    {
        requireActivity().supportFragmentManager
            .beginTransaction().add(R.id.layoutFrame, fragment).addToBackStack(null).commit()
    }



    private fun setupView() {


        val clickListener = object : ItemClickListener<ReturnRequestHistoryItem> {
            override fun onClick(view: View, position: Int, data: ReturnRequestHistoryItem) {

                when (view.id) {

                    R.id.icDownload -> {
                        if(hasDiskWritePermission()) {
                            val currentGuid = data.uploadedFileGuid ?: ""
                            blockingLoader.showDialog()

                            (viewModel as ReturnRequestHistoryViewModel).downloadFile(
                                currentGuid, model)
                        }
                    }

                    else -> addFragment(ProductDetailFragment.newInstance(productId = data.productId.toLong(), productName = data.productName))

                }
            }
        }

        rvDownloadableProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))

            listAdapter =
                ReturnRequestHistoryAdapter(
                    clickListener,
                    requireActivity()
                )
            adapter = listAdapter
        }
    }

    inner class ReturnRequestHistoryAdapter(
        private val clickListener: ItemClickListener<ReturnRequestHistoryItem>,
        private val context: FragmentActivity
    )
        : RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {

        private val list: MutableList<ReturnRequestHistoryItem> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_downloadable_product, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val data = list[position]

            holder.itemView.textView1.text = DbHelper.getString(Const.RETURN_ID).plus(" ").plus(data.id)
                .plus(" - ").plus(data.returnRequestStatus)


            holder.itemView.textView2.text =

            DbHelper.getString(Const.RETURNED_ITEM).plus(" : ").plus(data.productName).plus(" * ").plus(data.quantity)
                .plus("\n")
                .plus(DbHelper.getString(Const.RETURN_REASON).plus(" : ").plus(data.returnReason)
                .plus("\n")
                .plus(DbHelper.getString(Const.RETURN_ACTION).plus(" : ").plus(data.returnAction)
                .plus("\n"))
                .plus(DbHelper.getString(Const.RETURN_DATE_REQUESTED).plus(" : ").plus( TextUtils().tzTimeConverter(data.createdOn, WeakReference(requireActivity()))
                .plus("\n"))))
                /*.plus(DbHelper.getString(Const.RETURN_UPLOADED_FILE)).plus(" : ").plus("File")
                    )))*/


            holder.itemView.setOnClickListener { v ->
                clickListener.onClick(v, position, data)
            }

            holder.itemView.icDownload.setOnClickListener { v ->
                clickListener.onClick(v, position, data)
            }


        }

        fun addData(items: List<ReturnRequestHistoryItem>?) {

            list.clear()

            if(!items.isNullOrEmpty()) {
                list.addAll(items)
            }

            notifyDataSetChanged()
        }



    }
}