package com.bs.ecommerce.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.data.ProductSummary
import com.bs.ecommerce.utils.ItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_featured_product.view.*

class ProductListAdapter (
    private val productsList: List<ProductSummary>,
    private val listener: ItemClickListener<ProductSummary>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_featured_product, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvProductName.text = productsList[position].name
        holder.itemView.tvProductPrice.text = productsList[position].productPrice?.price

        holder.itemView.ratingBar.rating =
            (productsList[position].reviewOverviewModel?.ratingSum ?: 0).toFloat()

        Picasso.with(holder.itemView.context).load(
            productsList[position].defaultPictureModel?.imageUrl
                ?: "https://picsum.photos/300/300"
        ).fit().centerInside().into(holder.itemView.ivProductThumb)

        holder.itemView.setOnClickListener { v->
            listener.onClick(v, position, productsList[position])
        }
    }
}