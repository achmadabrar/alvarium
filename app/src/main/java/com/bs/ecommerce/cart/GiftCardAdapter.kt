package com.bs.ecommerce.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.model.data.GiftCard
import java.util.ArrayList

class GiftCardAdapter(context: Context, giftCardList: List<GiftCard>) : androidx.recyclerview.widget.RecyclerView.Adapter<GiftCardAdapter.ItemHolder>() {
    var giftCardList: MutableList<GiftCard>? = null

    protected lateinit var context: Context

    init {
        try {
            this.giftCardList = ArrayList()
            this.giftCardList!!.addAll(giftCardList)
            this.context = context
            Log.d("adapterSize", "" + giftCardList.size)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val layout: Int
        layout = R.layout.item_gift_card_list

        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val giftCard = giftCardList!![position]
        holder.gift_card_amount.text = giftCard.amount

        holder.gift_card_number.text = context.getString(R.string.gift_card).apply {
            if(!giftCard.remaining.isNullOrEmpty())
                plus(giftCard.remaining)
                    .plus(" ")
                    .plus(context.getString(R.string.remaining))
        }
    }


    override fun getItemCount() = giftCardList?.size!!


    inner class ItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var gift_card_number: TextView
        var gift_card_amount: TextView


        init {
            gift_card_number = itemView.findViewById<View>(R.id.gift_card_number) as TextView
            gift_card_amount = itemView.findViewById<View>(R.id.gift_card_amount) as TextView

        }

    }
}