package com.bs.ecommerce.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.data.Manufacturer
import com.bs.ecommerce.utils.ItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_manufacturer.view.*

class ManufacturerListAdapter(
    private val productsList: List<Manufacturer>,
    private val listener: ItemClickListener<Manufacturer>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_manufacturer, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvCompanyName.text = productsList[position].name

        Picasso.with(holder.itemView.context).load(
            productsList[position].pictureModel?.imageUrl
                ?: "https://picsum.photos/300/300"
        ).fit().centerInside().into(holder.itemView.ivLogo)

        holder.itemView.setOnClickListener { v ->
            listener.onClick(v, position, productsList[position])
        }
    }
}