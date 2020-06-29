package com.bs.ecommerce.account.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.model.data.WishListItem
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.TextUtils
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

        val item = list[position]

        with(holder.itemView) {

            id = R.id.itemView
            setOnClickListener { clickListener.onClick(it, position, item) }

            tvProductName.text = item.productName
            tvProductPrice.text = item.subTotal

            ivProductThumb.loadImg(item.picture?.imageUrl)

            tvCustomAttribute.text = TextUtils()
                .getHtmlFormattedText(item.attributeInfo)

            btnAddToCart.text = DbHelper.getString(Const.PRODUCT_BTN_ADDTOCART)
            btnAddToCart.setOnClickListener { v ->
                clickListener.onClick(v, position, item)
            }

            icRemoveItem.setOnClickListener { v ->
                clickListener.onClick(v, position, item)
            }
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