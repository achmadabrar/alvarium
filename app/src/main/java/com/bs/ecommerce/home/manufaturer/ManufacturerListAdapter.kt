package com.bs.ecommerce.home.manufaturer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.home.homepage.model.data.Manufacturer
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
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

        holder.itemView.ivLogo.loadImg(productsList[position].pictureModel?.imageUrl)

        holder.itemView.setOnClickListener { v ->
            listener.onClick(v, position, productsList[position])
        }
    }
}