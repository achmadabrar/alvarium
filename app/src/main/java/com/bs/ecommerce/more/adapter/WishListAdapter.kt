package com.bs.ecommerce.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.WishListItem
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_wish_list.view.*

class WishListAdapter(
    private val clickListener: ItemClickListener<WishListItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<WishListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wish_list, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvProductName.text = list[position].productName
        holder.itemView.tvProductPrice.text = list[position].subTotal

        holder.itemView.ivProductThumb.loadImg(list[position].picture?.imageUrl)

        holder.itemView.btnAddToCart.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }

        holder.itemView.icRemoveItem.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }
    }

    fun addData(items: List<WishListItem>?) {

        list.clear()

        if(!items.isNullOrEmpty()) {
            list.addAll(items)
        }

        notifyDataSetChanged()
    }

}