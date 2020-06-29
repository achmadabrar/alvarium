package com.bs.ecommerce.more.vendor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.model.data.ProductByVendorData
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.item_vendor_info.view.*

class VendorAdapter(
    private val listener: ItemClickListener<ProductByVendorData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = mutableListOf<ProductByVendorData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vendor_info, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vendor = list[position]

        holder.itemView.apply {
            id = R.id.itemView

            ivVendorThumb?.loadImg(vendor.pictureModel?.imageUrl)

            tvVendorName?.text = vendor.name
            tvVendorDetails?.text = vendor.description

            if(vendor.allowCustomersToContactVendors == true) {
                btnContactVendor?.text = DbHelper.getString(Const.VENDOR_CONTACT_VENDOR)
                btnContactVendor?.visibility = View.VISIBLE

                btnContactVendor?.setOnClickListener {
                    listener.onClick(btnContactVendor, position, vendor)
                }
            } else {
                btnContactVendor?.visibility = View.GONE
            }

            setOnClickListener { listener.onClick(holder.itemView, position, vendor) }
        }
    }

    fun addData(newData: List<ProductByVendorData>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }
}