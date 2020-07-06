package com.bs.ecommerce.cart

import android.content.Context
import android.graphics.Paint
import android.view.*
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.product_price_layout_for_list.view.*
import java.util.*


open class CartAdapter(
    val context: FragmentActivity,
    productsList: List<CartProduct>,
    private val clickListener: ItemClickListener<CartProduct>?,
    val viewModel: BaseViewModel,
    val model: CartModel? = null,
    var isCheckout: Boolean = false
)

    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var products: List<CartProduct>
    protected var mItemClickListener: OnItemClickListener? = null

    var holder: ProductSummaryHolder? = null// = (ProductSummaryHolder) bindViewHolder;
    lateinit var productModel: CartProduct

    init {
        try {
            this.products = ArrayList()
            (this.products as ArrayList<CartProduct>).addAll(productsList)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item, parent, false)
        return ProductSummaryHolder(itemView)
    }

    override fun getItemViewType(position: Int) = 0

    private fun setTouchListener(holder: ProductSummaryHolder, productModel: CartProduct) {

        val editText = holder.productQuantity

        holder.qunatityUpImageView.setOnClickListener { updateQuntity(1, editText, productModel) }

        holder.qunatityDownImageView.setOnClickListener {
            updateQuntity(
                -1,
                editText,
                productModel
            )
        }
    }

    private fun updateQuntity(quantity: Int, textView: TextView, productModel: CartProduct)
    {
        val previousQuantity = Integer.parseInt(textView.text.toString())
        val totalQuntity = previousQuantity + quantity

        if (totalQuntity > 0)
            updateCartItem("itemquantity" + productModel.id, "" + totalQuntity)


    }

    private fun updateCartItem(key: String, value: String) {
        val keyValuePairList = ArrayList<KeyValuePair>()

        keyValuePairList.add(
            KeyValuePair(
                key = key,
                value = value
            )
        )

        (viewModel as CartViewModel).updateCartData(keyValuePairList, model!!)
    }

    override fun onBindViewHolder(
        bindViewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        try {
            if (bindViewHolder is ProductSummaryHolder) {

                productModel = products[position]
                holder = bindViewHolder

                holder?.itemView?.id = R.id.itemView
                holder?.itemView?.setOnClickListener { clickListener?.onClick(it, position, products[position]) }

                holder?.productName?.text = productModel.productName

                holder?.productTotalPrice?.text = "${DbHelper.getString(Const.TOTAL)}:  ${productModel.subTotal}"
                holder?.productPrice?.text = "${DbHelper.getString(Const.PRICE)}:  ${productModel.unitPrice}"


                holder?.productImage?.loadImg(productModel.picture?.imageUrl)


                if(isCheckout)
                {
                    holder?.removeItem?.visibility = View.GONE
                    holder?.quantityLayout?.visibility = View.GONE

                    holder?.quantityForCheckout?.visibility = View.VISIBLE
                    holder?.quantityForCheckout?.text = "${DbHelper.getString(Const.QUANTITY)}:  ${productModel.quantity}"
                }
                else
                    holder?.productQuantity!!.text = productModel.quantity.toString()

                if(productModel.attributeInfo.isNotEmpty())
                {
                    holder?.tvAttribute1?.visibility = View.VISIBLE
                    holder?.tvAttribute1?.show(productModel.attributeInfo, R.color.list_item_bg)
                }
                else
                    holder?.tvAttribute1?.visibility =View.GONE

                productModel.warnings?.let {
                    if(it.isNotEmpty())
                        context.toast(it[0])
                }


                OntrashClicked(holder!!.removeItem, position)

                setTouchListener(holder!!, productModel)
            }


        } catch (ex: ClassCastException) {
        }


    }

    private fun disableTextBoxSelection(productQuantity: TextView) {
        productQuantity.customSelectionActionModeCallback = object : ActionMode.Callback {

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onDestroyActionMode(mode: ActionMode) = Unit

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = false
        }
    }

    private fun OntrashClicked(itemview: ImageView, position: Int) {
        itemview.setOnClickListener { updateCartItem("removefromcart", "" + products[position].id) }
    }


    override fun getItemCount(): Int = products.size ?: 0


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }


    inner class ProductSummaryHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var productImage: ImageView
        var productTotalPrice: TextView
        var productPrice: TextView
        var productName: TextView

        var quantityLayout : LinearLayout

        var productQuantity: TextView
        var quantityForCheckout: TextView


        var removeItem: ImageView


        var qunatityUpImageView: Button
        var qunatityDownImageView: Button

        var tvAttribute1: WebView


        init {

            quantityLayout = itemView.findViewById<View>(R.id.ll_quantity) as LinearLayout
            productImage = itemView.findViewById<View>(R.id.ivProductThumb) as ImageView
            productTotalPrice = itemView.findViewById<View>(R.id.tvTotalPrice) as TextView
            productPrice = itemView.findViewById<View>(R.id.tvDiscountPrice) as TextView
            productName = itemView.findViewById<View>(R.id.tvProductName) as TextView
            // productShortdescription = itemView.findViewById<View>(R.id.tv_product_short_descrption) as TextView
            productQuantity = itemView.findViewById<View>(R.id.tvItemQuantity) as TextView
            quantityForCheckout = itemView.findViewById<View>(R.id.quantityForCheckout) as TextView
            // fav = itemView.findViewById<View>(R.id.fav) as CheckBox
            removeItem = itemView.findViewById<View>(R.id.icRemoveItem) as ImageView
            //swipeLayout=(SwipeLayout)itemView.findViewById(R.id.swipe);
            //trash = (itemView.findViewById<View>(R.id.trash) as ImageView?)
            qunatityUpImageView = itemView.findViewById<View>(R.id.btnPlus) as Button
            qunatityDownImageView = itemView.findViewById<View>(R.id.btnMinus) as Button

            tvAttribute1 = itemView.findViewById<View>(R.id.tvAttribute1) as WebView
            //itemView.tvAttribute1.text = "Color: Black"
            itemView.sku.text = "Size: XL"
/*
            itemView.tvOriginalPrice.text = "$100"
            itemView.tvOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG*/

            removeItem.setOnClickListener(this)
            //swipeLayout.getSurfaceView().setOnClickListener(this);

            itemView.setOnClickListener(this)

        }


        override fun onClick(v: View) {
            mItemClickListener?.onItemClick(v, adapterPosition)
        }

    }
}
