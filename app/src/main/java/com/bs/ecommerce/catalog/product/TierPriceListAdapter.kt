package com.bs.ecommerce.catalog.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.catalog.common.TierPriceItem
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.item_product_tier_price.view.*

class TierPriceListAdapter(
    private val context: FragmentActivity,
    private val tierPriceList: List<TierPriceItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val listWithTableHeader: MutableList<TierPriceItem> = mutableListOf()

    init {
        listWithTableHeader.add(
            TierPriceItem(
                price = "",
                quantity = 0
            )
        )
        listWithTableHeader.addAll(tierPriceList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_tier_price, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = listWithTableHeader.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val tierPriceItem = listWithTableHeader[position]

        if(position == 0)
        {
            holder.itemView.quantityItem.text = DbHelper.getString(Const.QUANTITY)
            holder.itemView.priceItem.text = DbHelper.getString(Const.PRICE)
            holder.itemView.startDivider.visibility = View.VISIBLE
        }
        else
        {
            holder.itemView.quantityItem.text = "${tierPriceItem.quantity}+"
            holder.itemView.priceItem.text = tierPriceItem.price

            holder.itemView.startDivider.visibility = View.GONE
        }
    }

}