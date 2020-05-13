package com.bs.ecommerce.product.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.SubCategory
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_product_for_gridlayout.view.*

class SubcategoryListAdapter (
    private val subcatList: List<SubCategory>,
    private val listener: ItemClickListener<SubCategory>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_for_gridlayout, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = subcatList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvProductName.text = subcatList[position].name

        holder.itemView.ratingBar.visibility = View.GONE
        holder.itemView.ivAddToFav.visibility = View.GONE

        holder.itemView.ivProductThumb.loadImg(
            subcatList[position].pictureModel?.imageUrl
        )

        holder.itemView.setOnClickListener { v->
            listener.onClick(v, position, subcatList[position])
        }
    }
}