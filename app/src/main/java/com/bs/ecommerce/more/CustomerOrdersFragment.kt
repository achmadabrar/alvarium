package com.bs.ecommerce.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_customer_order.*
import kotlinx.android.synthetic.main.item_customer_order.view.*

class CustomerOrdersFragment : BaseFragment(){

    override fun getFragmentTitle() = R.string.title_orders

    override fun getLayoutId(): Int = R.layout.fragment_customer_order

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateOrderList()
    }

    private fun populateOrderList() {
        val items: MutableList<TempItem> = mutableListOf()

        for (i in 1..4) {
            items.add(TempItem("0$i.04.2020", "$${i * 167}"))
        }

        rvOrders?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            //addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = TempAdapter(items)
        }
    }

    //-----------------------

    inner class TempAdapter(
        private val list: List<TempItem>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_customer_order, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tvOrderNumber.text = "Order Number $position"
            holder.itemView.tvOrderDate.text = "Order Date: ${list[position].date}"
            holder.itemView.tvOrderStatus.text = "Order Status: Delivered"
            holder.itemView.tvOrderTotal.text = "Order Price: ${list[position].price}"

//            Picasso.with(holder.itemView.context).load("https://picsum.photos/300/300")
//                .fit().centerInside().into(holder.itemView.ivOptionIcon)

            holder.itemView.setOnClickListener {
                replaceFragmentSafely(CustomerOrderDetailsFragment())
            }
        }

    }

    inner class TempItem(
        val date: String,
        val price: String
    )
}