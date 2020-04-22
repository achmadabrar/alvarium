package com.bs.ecommerce.cart

import android.graphics.Paint
import android.view.*
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartProduct
import com.bs.ecommerce.utils.Language
import com.bs.ecommerce.utils.show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.product_price_layout.view.*
import java.util.*


open class CartAdapter(
    productsList: List<CartProduct>,
    fragment: Fragment,
    val viewModel: BaseViewModel,
    val model: CartModel,
    var isCheckout: Boolean = false
)

    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var products: List<CartProduct>
    protected var mItemClickListener: OnItemClickListener? = null
    lateinit var fragment: androidx.fragment.app.Fragment

    var holder: ProductSummaryHolder? = null// = (ProductSummaryHolder) bindViewHolder;
    lateinit var productModel: CartProduct

    init {
        try {
            this.products = ArrayList()
            (this.products as ArrayList<CartProduct>).addAll(productsList)
            this.fragment = fragment

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item, parent, false)
        return ProductSummaryHolder(itemView)
    }

    override fun getItemViewType(position: Int) = 0

    private fun setTouchListener(holder: ProductSummaryHolder, productModel: CartProduct) {

        val editText = holder.productQuantity

        /*if (productModel.quantity == 1) {
            holder.qunatityDownImageView.alpha = 0.7F
            holder.qunatityDownImageView.invalidate()
        }*/

        holder.qunatityUpImageView.setOnClickListener { updateQuntity(1, editText, productModel) }

        holder.qunatityDownImageView.setOnClickListener {
            updateQuntity(
                -1,
                editText,
                productModel
            )
        }
    }

    private fun updateQuntity(
        quantity: Int, textView: TextView,
        productModel: CartProduct
    ) {
        val previousQuantity = Integer.parseInt(textView.text.toString())
        val totalQuntity = previousQuantity + quantity
        if (totalQuntity > 0) {
            updateCartItem("itemquantity" + productModel.id, "" + totalQuntity)
        }

    }

    private fun updateCartItem(key: String, value: String) {
        val keyValuePairs = ArrayList<KeyValuePair>()
        KeyValuePair().apply {

            this.key = key
            this.value = value
            keyValuePairs.add(this)
        }

        (viewModel as CartViewModel).updateCartData(keyValuePairs, model)
    }

    override fun onBindViewHolder(
        bindViewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        try {
            if (bindViewHolder is ProductSummaryHolder) {

                productModel = products[position]
                holder = bindViewHolder


                holder?.productName!!.text = productModel.productName
                holder?.productPrice!!.text = productModel.unitPrice
                //holder?.productShortdescription!!.visibility = View.GONE
                holder?.productQuantity!!.text = productModel.quantity.toString()

                Picasso.with(holder?.itemView?.context).load(productModel.picture?.imageUrl)
                    .fit().centerInside().into(holder?.productImage)

                //holder?.fav!!.tag = position

                if(isCheckout)
                {
                    holder?.removeItem?.visibility = View.GONE
                    holder?.quantityLayout?.visibility = View.GONE

                    holder?.quantityForCheckout?.visibility = View.VISIBLE
                    holder?.quantityForCheckout?.text = "Quantity:  ${productModel.quantity}"
                }

                if(productModel.attributeInfo.isNotEmpty())
                {
                    holder?.tvAttribute1?.visibility = View.VISIBLE
                    holder?.tvAttribute1?.show(productModel.attributeInfo)
                }
                else
                    holder?.tvAttribute1?.visibility =View.GONE



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
            itemView.tvAttribute2.text = "Size: XL"
            itemView.tvOriginalPrice.text = "$100"
            itemView.tvOriginalPrice.paintFlags = itemView.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            removeItem.setOnClickListener(this)
            //swipeLayout.getSurfaceView().setOnClickListener(this);

            itemView.setOnClickListener(this)

        }


        override fun onClick(v: View) {
            mItemClickListener?.onItemClick(v, adapterPosition)
        }

    }
}
