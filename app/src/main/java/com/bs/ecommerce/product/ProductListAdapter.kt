package com.bs.ecommerce.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_product_for_gridlayout.view.*

class ProductListAdapter(
    private val listener: ItemClickListener<ProductSummary>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val productsList: MutableList<ProductSummary> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_for_gridlayout, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvProductName.text = productsList[position].name
        holder.itemView.tvProductPrice.text = productsList[position].productPrice?.price

        holder.itemView.ratingBar.rating =
            (productsList[position].reviewOverviewModel?.ratingSum ?: 0).toFloat()

        holder.itemView.ivProductThumb.loadImg(
            productsList[position].defaultPictureModel?.imageUrl
        )

        holder.itemView.id = R.id.itemView
        holder.itemView.setOnClickListener { v ->
            listener.onClick(v, position, productsList[position])
        }

        holder.itemView.ivAddToFav.setOnClickListener { v ->
            listener.onClick(v, position, productsList[position])
        }
    }

    fun addData(list: List<ProductSummary>?, shouldAppend: Boolean) {

        if(list == null) return

        if(shouldAppend) {
            val previousSize = itemCount
            productsList.addAll(list)

            notifyItemRangeInserted(previousSize, list.size)
        } else {
            productsList.clear()
            productsList.addAll(list)

            notifyDataSetChanged()
        }
    }
}