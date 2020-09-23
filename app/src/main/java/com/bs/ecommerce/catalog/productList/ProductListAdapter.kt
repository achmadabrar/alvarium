package com.bs.ecommerce.catalog.productList

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.catalog.common.ProductSummary
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

        val product = productsList[position]

        holder.itemView.apply {
            tvProductName?.text = product.name

            val currentPrice: Spannable = SpannableString(product.productPrice?.price ?: "")
            val oldPrice: Spannable = SpannableString(product.productPrice?.oldPrice ?: "")

            try {
                currentPrice.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.themePrimary)), 0, currentPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvProductPrice.text = currentPrice

                oldPrice.setSpan(
                    StrikethroughSpan(), 0, oldPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvProductPrice.append(" ")
                tvProductPrice.append(oldPrice)
            } catch (e: Exception) {
                tvProductPrice.text = currentPrice
            }

            if(product.reviewOverviewModel?.allowCustomerReviews == true) {
                ratingBar?.visibility = View.VISIBLE
                ratingBar?.rating = (product.reviewOverviewModel.ratingSum ?: 0).toFloat()
            } else {
                ratingBar?.visibility = View.GONE
            }

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