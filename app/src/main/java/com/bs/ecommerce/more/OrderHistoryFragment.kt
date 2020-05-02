package com.bs.ecommerce.more

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
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.model.OrderModel
import com.bs.ecommerce.more.model.OrderModelImpl
import com.bs.ecommerce.more.viewmodel.OrderViewModel
import com.bs.ecommerce.product.model.data.Order
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_customer_order.*
import kotlinx.android.synthetic.main.item_customer_order.view.*

class OrderHistoryFragment : BaseFragment() {
    private lateinit var model: OrderModel

    override fun getFragmentTitle() = R.string.title_orders

    override fun getLayoutId(): Int = R.layout.fragment_customer_order

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel = OrderViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            "nop_".showLog("creating view")

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

            isLoadingLD.observe(
                viewLifecycleOwner,
                Observer { isShowLoader ->

                    if (isShowLoader)
                        showLoading()
                    else
                        hideLoading()
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

            holder.itemView.tvOrderNumber.text =
                getString(R.string.order_number).plus(order.customOrderNumber)

            holder.itemView.tvOrderDate.text = getString(R.string.order_date).plus(
                TextUtils().tzTimeConverter(
                    order.createdOn,
                    requireContext()
                )
            )

            holder.itemView.tvOrderStatus.text =
                getString(R.string.order_status).plus(order.orderStatus)

            holder.itemView.tvOrderTotal.text =
                getString(R.string.order_total).plus(order.orderTotal)

            holder.itemView.setOnClickListener {
                if (order.id != null)
                    replaceFragmentSafely(OrderDetailsFragment.newInstance(order.id))
            }
        }

    }
}