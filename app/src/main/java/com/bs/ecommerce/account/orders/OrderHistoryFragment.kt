package com.bs.ecommerce.account.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.account.orders.model.OrderModel
import com.bs.ecommerce.account.orders.model.OrderModelImpl
import com.bs.ecommerce.account.orders.model.data.Order
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_customer_order.*
import kotlinx.android.synthetic.main.item_customer_order.view.*
import java.lang.ref.WeakReference

class OrderHistoryFragment : BaseFragment() {
    private lateinit var model: OrderModel

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_ORDERS)

    override fun getLayoutId(): Int = R.layout.fragment_customer_order

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel =
        OrderViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            "nop_".showLog("creating view")
            tvNoData.text = DbHelper.getString(Const.COMMON_NO_DATA)

            model = OrderModelImpl()
            viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

            (viewModel as OrderViewModel).getOrderHistory(model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as OrderViewModel).apply {

            orderHistoryLD.observe(
                viewLifecycleOwner,
                Observer { orderHistoryData ->

                    if (orderHistoryData.orders.isNullOrEmpty()) {
                        tvNoData.visibility = View.VISIBLE
                    } else {
                        tvNoData.visibility = View.GONE
                        populateOrderList(orderHistoryData.orders)
                    }
                })

            isLoadingLD.observe(viewLifecycleOwner, Observer {
                    isShowLoader -> showHideLoader(isShowLoader)
            })
        }
    }

    private fun populateOrderList(orders: List<Order>) {

        rvOrders?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            //addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = OrderHistoryAdapter(orders)
        }
    }



    inner class OrderHistoryAdapter(
        private val list: List<Order>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_customer_order, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val order = list[position]

            holder.itemView.apply {

                tvOrderNumber?.text =
                    DbHelper.getString(Const.ORDER_NUMBER).plus(" ").plus(order.customOrderNumber)

                tvOrderDate?.text = DbHelper.getString(Const.ORDER_DATE).plus(" ").plus(
                    TextUtils().tzTimeConverter(
                        order.createdOn,
                        WeakReference(requireContext())
                    )
                )

                tvOrderStatus?.text =
                    DbHelper.getString(Const.ORDER_STATUS).plus(" ").plus(order.orderStatus)

                tvOrderTotal?.text =
                    DbHelper.getString(Const.ORDER_TOTAL).plus(" ").plus(order.orderTotal)


                setOnClickListener {
                    if (order.id != null)
                        replaceFragmentSafely(OrderDetailsFragment.newInstance(order.id))
                }


                // Return request action

                if(order.isReturnRequestAllowed == true) {

                    btnReqReturn?.visibility = View.VISIBLE

                    btnReqReturn?.text = DbHelper.getString(Const.ORDER_RETURN_ITEMS)

                    btnReqReturn.setOnClickListener {
                        replaceFragmentSafely(ReturnRequestFragment.newInstance(order.id ?: -1))
                    }
                } else {
                    btnReqReturn?.visibility = View.GONE
                }

            }
        }

    }
}