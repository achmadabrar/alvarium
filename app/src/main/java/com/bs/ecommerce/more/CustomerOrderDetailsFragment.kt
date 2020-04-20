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
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_customer_order_detail.*
import kotlinx.android.synthetic.main.product_price_layout.view.*

class CustomerOrderDetailsFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_customer_order_detail

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        orderDetailsCard.tvCardTitle.text = "Order Details"
        orderDetailsCard.tvCardDetails.text =
            "Order Number: 760\nOrder Status: Delivered\nOrder Date: 04.04.2020\nOrder Total: $780"
        orderDetailsCard.ivCardThumb.visibility = View.GONE

        shippingAddressCard.tvCardTitle.text = getString(R.string.shipping_address)
        shippingAddressCard.tvCardDetails.text =
            "Name: John Doe\nEmail: x@y.com\nAddress: Khilgaon, Tilpapara\nCity: Dhaka\nCountry: Bangladesh"
        shippingAddressCard.ivCardThumb.visibility = View.GONE

        billingAddressCard.tvCardTitle.text = getString(R.string.billing_address)
        billingAddressCard.tvCardDetails.text =
            "Name: John Doe\nEmail: x@y.com\nAddress: Khilgaon, Tilpapara\nCity: Dhaka\nCountry: Bangladesh"
        billingAddressCard.ivCardThumb.visibility = View.GONE

        shippingMethodCard.tvCardTitle.text = "Shipping Method"
        shippingMethodCard.tvCardDetails.text = "Grounded\nNot yet shippied"
        shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

        paymentMethodCard.tvCardTitle.text = "Payment Method"
        paymentMethodCard.tvCardDetails.text = "VISA\nPending"
        paymentMethodCard.ivCardThumb.visibility = View.VISIBLE

        // mock data
        val data = mutableListOf<TempItem>()

        for(i in 1..3) {
            data.add(TempItem("Item $i", (i*14).toString()))
        }

        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = TempAdapter(data)
        }
    }

    //--------------------

    inner class TempAdapter(
        private val list: List<TempItem>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cart_list_item, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tvOriginalPrice.text = list[position].price
            holder.itemView.tvProductName.text = list[position].date
//            holder.itemView.tvDiscountPercent.text = "Order Status: Delivered"
            holder.itemView.tvDiscountPrice.text = list[position].price

            holder.itemView.ivProductThumb.loadImg("https://picsum.photos/300/300")
        }

    }

    inner class TempItem(
        val date: String,
        val price: String
    )
}