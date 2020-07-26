package com.bs.ecommerce.account.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.account.orders.model.data.ReturnReqProductItem
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import kotlinx.android.synthetic.main.item_return_req.view.*
import kotlinx.android.synthetic.main.qunatity_button.view.*

class ReturnReqProductAdapter(
    private val list: List<ReturnReqProductItem>,
    private val clickListener: ItemClickListener<ReturnReqProductItem>?
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_return_req, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val product = list[position]

        holder.itemView.apply {

            tvProductName?.text = product.productName
            tvProductPrice?.text = product.unitPrice
            tvTitle?.text = DbHelper.getString(Const.RETURN_REQ_RETURN_QTY)
            btn_holder?.tvItemQuantity?.text = product.customerInput.toString()

            btn_holder?.btnPlus?.setOnClickListener { v ->

                if(product.customerInput < product.quantity ?: 11111) {
                    product.customerInput++
                    btn_holder?.tvItemQuantity?.text = product.customerInput.toString()
                }
            }

            btn_holder?.btnMinus?.setOnClickListener { v ->

                if(product.customerInput > 0) {
                    product.customerInput--
                    btn_holder?.tvItemQuantity?.text = product.customerInput.toString()
                }
            }
        }
    }
}