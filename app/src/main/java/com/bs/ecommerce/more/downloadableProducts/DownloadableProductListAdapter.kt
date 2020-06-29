package com.bs.ecommerce.more.downloadableProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductItem
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.item_address.view.tv1
import kotlinx.android.synthetic.main.item_address.view.tv2
import kotlinx.android.synthetic.main.item_downloadable_product.view.*
import java.lang.ref.WeakReference

class DownloadableProductListAdapter(
    private val clickListener: ItemClickListener<DownloadableProductItem>,
    private val context: FragmentActivity
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private val list: MutableList<DownloadableProductItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_downloadable_product, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val product = list[position]

        holder.itemView.tv1.text = product.productName

        with(product)
        {
            holder.itemView.tv2.text =
                "${DbHelper.getString(Const.ORDER_NUMBER)}: $customOrderNumber\n" +
                    "${DbHelper.getString(Const.ORDER_DATE)}: ${TextUtils().tzTimeConverter(
                        createdOn,
                        WeakReference(context)
                    )}"

        }

        holder.itemView.icDownload?.visibility = if(product.downloadId == null || product.downloadId == 0)
            View.GONE else View.VISIBLE

        holder.itemView.icDownload.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }

        holder.itemView.tv1.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }

        holder.itemView.tv2.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }
    }

    fun addData(items: List<DownloadableProductItem>?) {

        list.clear()

        if(!items.isNullOrEmpty()) {
            list.addAll(items)
        }

        notifyDataSetChanged()
    }



}