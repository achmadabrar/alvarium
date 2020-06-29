package com.bs.ecommerce.account.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.AddressModel
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.item_address.view.*
import java.lang.ref.WeakReference

class AddressListAdapter(
    private val clickListener: ItemClickListener<AddressModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<AddressModel> = mutableListOf()
    private val textUtils = TextUtils()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_address, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val address = list[position]

        holder.itemView.tv1.text = address.firstName.plus(" ").plus(address.lastName)

        holder.itemView.tv2.text = textUtils.getFormattedAddress(
            list[position], WeakReference(holder.itemView.tv2.context)
        )

        holder.itemView.icEdit.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }

        holder.itemView.icDelete.setOnClickListener { v ->
            clickListener.onClick(v, position, list[position])
        }
    }

    fun addData(items: List<AddressModel>?) {

        list.clear()

        if(!items.isNullOrEmpty()) {
            list.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun removeData(position: Int) {

        if (position < 0 || position >= list.size)
            return

        list.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

}