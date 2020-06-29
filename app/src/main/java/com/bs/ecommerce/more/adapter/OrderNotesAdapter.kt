package com.bs.ecommerce.more.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.OrderNotes
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.item_order_notes.view.*
import java.lang.ref.WeakReference

class OrderNotesAdapter(
    private val list: List<OrderNotes>,
    private val listener: ItemClickListener<OrderNotes>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var textUtils: TextUtils = TextUtils()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_notes, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.tvNoteText?.text = item.note
        holder.itemView.tvNoteDate?.text = textUtils.tzTimeConverter(
            item.createdOn, WeakReference(holder.itemView.context)
        )

        holder.itemView.ivDownload?.apply {
            visibility = if(item.hasDownload == true) View.VISIBLE else View.GONE

            setOnClickListener { listener.onClick(this, position, item) }
        }
    }

}