package com.bs.ecommerce.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_associated_product.view.*
import kotlinx.android.synthetic.main.product_price_layout_for_list.view.*
import kotlinx.android.synthetic.main.qunatity_button.view.*


class AssociatedProductAdapter(
    private val productsList: List<ProductDetail>,
    private val clickListener: ItemClickListener<ProductDetail>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val quantityMap: MutableMap<Long, Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_associated_product, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }


    private fun updateQuantity(productId: Long, isIncrement: Boolean, textView: TextView) {

        val previousQ = quantityMap[productId] ?: 1

        val newQ = when {
            isIncrement -> previousQ + 1
            previousQ > 1 -> previousQ - 1
            else -> 1
        }

        quantityMap[productId] = newQ

        textView.text = newQ.toString()
    }

    private fun getQuantity(productId: Long?) : Int = quantityMap[productId] ?: 1

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val productModel = productsList[position]

        holder.itemView.apply {
            id = R.id.itemView

            tvProductName?.text = productModel.name
            tvDiscountPrice?.text = productModel.productPrice?.price
            tvOriginalPrice?.text = productModel.productPrice?.oldPrice
            tvItemQuantity?.text = "1"
            ivProductThumb?.loadImg(productModel.defaultPictureModel?.imageUrl)

            setOnClickListener {
                clickListener?.onClick(it, position, productModel)
            }

            ivAddToCart.setOnClickListener {
                productModel.quantity = getQuantity(productModel.id)
                clickListener?.onClick(ivAddToCart, position, productModel)
            }

            ivAddToWishList.setOnClickListener {
                productModel.quantity = getQuantity(productModel.id)
                clickListener?.onClick(ivAddToWishList, position, productModel)
            }

            btnPlus.setOnClickListener {
                updateQuantity(productModel.id!!, true, tvItemQuantity)
            }

            btnMinus.setOnClickListener {
                updateQuantity(productModel.id!!, false, tvItemQuantity)
            }
        }

    }

    override fun getItemCount(): Int = productsList.size

}
