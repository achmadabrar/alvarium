package com.bs.ecommerce.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_product_for_linearlayout.view.*

class FeaturedProductAdapter(
    private val productsList: List<ProductSummary>,
    private val listener: ItemClickListener<ProductSummary>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_for_linearlayout, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = productsList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = productsList[position]

        holder.itemView.apply {
            tvProductName?.text = product.name
            tvProductPrice?.text = product.productPrice?.price

            ratingBar?.rating = (product.reviewOverviewModel?.ratingSum ?: 0).toFloat()

            ivProductThumb?.loadImg(
                product.defaultPictureModel?.imageUrl
            )

            id = R.id.itemView
            setOnClickListener { v ->
                listener.onClick(v, position, product)
            }

            ivAddToFav?.setOnClickListener { v ->
                listener.onClick(v, position, product)
            }

            ivAddToFav?.visibility = if(product.productPrice?.disableWishlistButton == true)
                View.INVISIBLE else View.VISIBLE
        }
    }
}