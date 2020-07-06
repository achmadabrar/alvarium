package com.bs.ecommerce.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import com.bs.ecommerce.utils.show
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.product_price_layout_for_list.view.*
import kotlinx.android.synthetic.main.qunatity_button.view.*


class CartAdapter2 (
    private val list: MutableList<CartProduct> = mutableListOf(),
    private val clickListener: ItemClickListener<CartProduct>?,
    private val isCheckout: Boolean,
    private val showSku: Boolean,
    private val isEditable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_list_item, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]

        with(holder.itemView) {

            id = R.id.itemView
            setOnClickListener { clickListener?.onClick(it, position, item) }

            tvProductName?.text = item.productName
            productPriceLayout?.tvTotalPrice?.text = DbHelper.getString(Const.TOTAL).plus(": ").plus(item.subTotal)
            productPriceLayout?.tvDiscountPrice?.text = DbHelper.getString(Const.PRICE).plus(": ").plus(item.unitPrice)

            ivProductThumb?.loadImg(item.picture?.imageUrl)


            icRemoveItem?.visibility = if (isCheckout) View.GONE else View.VISIBLE
            btn_holder?.visibility = if (isCheckout) View.GONE else View.VISIBLE

            quantityForCheckout?.visibility = if (isCheckout) View.VISIBLE else View.GONE
            quantityForCheckout?.text =
                DbHelper.getString(Const.QUANTITY).plus(": ").plus(item.quantity)

            tvItemQuantity?.text = if (isCheckout) "" else item.quantity.toString()

            sku?.text = DbHelper.getString(Const.SKU).plus(": ").plus(item.sku)
            sku?.visibility = if (showSku) View.VISIBLE else View.GONE




            if (item.attributeInfo.isNotEmpty()) {
                tvAttribute1?.visibility = View.VISIBLE
                tvAttribute1?.show(item.attributeInfo, R.color.list_item_bg)
            } else
                tvAttribute1?.visibility = View.GONE

            item.warnings?.let {
                if(it.isNotEmpty())
                    Toast.makeText(holder.itemView.context, it[0], Toast.LENGTH_SHORT).show()
            }

            icRemoveItem?.setOnClickListener { v ->
                clickListener?.onClick(v, position, item)
            }

            btn_holder?.btnPlus?.setOnClickListener { v ->
                clickListener?.onClick(v, position, item)
            }

            btn_holder?.btnMinus?.setOnClickListener { v ->
                clickListener?.onClick(v, position, item)
            }
        }
    }

    fun addData(items: List<CartProduct>?) {

        list.clear()

        if(!items.isNullOrEmpty()) {
            list.addAll(items)
        }

        notifyDataSetChanged()
    }

}