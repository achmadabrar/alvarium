package com.bs.ecommerce.account.orders.shipments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.model.data.ShipmentItem
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import java.lang.ref.WeakReference

class ShipmentListAdapter(
    private val list: List<ShipmentItem>,
    private val context: FragmentActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.confirm_order_card, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]


        with(holder.itemView)
        {
            tvCardTitle.visibility = View.GONE

            tvCardDetails.text = DbHelper.getString(Const.ORDER_SHIPMENT_ID).plus(" ") + item.trackingNumber

            tvCardDetails2.visibility = View.VISIBLE

            tvCardDetails2.text = DbHelper.getString(Const.ORDER_TRACKING_NUMBER).plus(" : ").plus(item.trackingNumber).plus("\n")
            .plus(
                DbHelper.getString(Const.ORDER_DATE_SHIPPED).plus(" : ").plus( TextUtils().tzTimeConverter(item.shippedDate, WeakReference(context)
            ).plus("\n"))
                .plus(

                    DbHelper.getString(Const.ORDER_DATE_DELIVERED).plus(" : ").plus( TextUtils().tzTimeConverter(item.deliveryDate, WeakReference(context)))
                ))
        }
    }
}