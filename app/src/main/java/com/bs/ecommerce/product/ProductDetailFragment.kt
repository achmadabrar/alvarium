package com.bs.ecommerce.product

import android.R.attr.numColumns
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.ColorSelectionProcess
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.color_selection_layout.view.*
import kotlinx.android.synthetic.main.featured_product_layout.view.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.item_featured_product.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.*
import kotlinx.android.synthetic.main.product_name_layout.view.tvProductName
import kotlinx.android.synthetic.main.product_price_layout.view.*
import kotlinx.android.synthetic.main.product_quantity.view.*


class ProductDetailFragment : BaseFragment() {
    private lateinit var colorSelectionProcess: ColorSelectionProcess
    lateinit var sizeSelectionProcess: ColorSelectionProcess
    var dynamicViewId = id

    override fun getLayoutId(): Int = R.layout.fragment_product_detail

    override fun getRootLayout(): RelativeLayout = productDetailsRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        productNameLayout.tvProductName.text = "Xiaomi Redmi 8A"
        productNameLayout.tvProductDescription.text = getString(R.string.placeholder_long)

        productPriceLayout.tvDiscountPrice.text = "$200"
        productPriceLayout.tvOriginalPrice.text = "$500"
        productPriceLayout.tvOriginalPrice.paintFlags =
            productPriceLayout.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        productPriceLayout.tvDiscountPercent.text = "40% Off"

        tvAvailability.text = "In Stock"

        productQuantityLayout.tvQuantity.text = "2"

        productDescLayout.tvProductName.text = "Description"
        productDescLayout.tvProductDescription.text = getString(R.string.placeholder_long)

        //
        val items: MutableList<FeaturedProduct> = mutableListOf()

        for (i in 1..5) {
            items.add(FeaturedProduct("Product $i", "$${i * 167}"))
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        similarProductList.rvFeaturedProduct?.setHasFixedSize(true)
        similarProductList.rvFeaturedProduct?.layoutManager = layoutManager

        val decoration = RecyclerViewMargin(15, numColumns, false)
        similarProductList.rvFeaturedProduct?.addItemDecoration(decoration)

        similarProductList.rvFeaturedProduct?.adapter = TempAdapter(items)

        // Attribute - color selection
        colorSelectionLayout.tvLayoutTitle.text = getString(R.string.color)
        colorSelectionLayout.tvLayoutSubTitle.text = getString(R.string.select_color)
        colorSelectionProcess = ColorSelectionProcess(colorSelectionLayout.radioGridGroup)

        generateColorSelectionRB(FeaturedProduct("", "#10CB3C"))
        generateColorSelectionRB(FeaturedProduct("", "#E46CA6"))
        generateColorSelectionRB(FeaturedProduct("", "#73BFF1"))
        generateColorSelectionRB(FeaturedProduct("", "#F3C442"))
        generateColorSelectionRB(FeaturedProduct("", "#A9AABF"))
        generateColorSelectionRB(FeaturedProduct("", "#42F3DB"))

        sizeSelectionLayout.tvLayoutTitle.text = getString(R.string.size)
        sizeSelectionLayout.tvLayoutSubTitle.text = getString(R.string.select_size)
        sizeSelectionProcess = ColorSelectionProcess(sizeSelectionLayout.radioGridGroup)

        generateSizeSelectionRB(FeaturedProduct("S", "#10CB3C"))
        generateSizeSelectionRB(FeaturedProduct("M", "#E46CA6"))
        generateSizeSelectionRB(FeaturedProduct("L", "#73BFF1"))
        generateSizeSelectionRB(FeaturedProduct("XL", "#F3C442"))
        generateSizeSelectionRB(FeaturedProduct("XXL", "#A9AABF"))
    }

    private fun generateColorSelectionRB(method: FeaturedProduct) {
        val radioButton = layoutInflater.inflate(
            R.layout.radiobutton_product_color, colorSelectionLayout.radioGridGroup, false
        ) as AppCompatRadioButton


        radioButton.text = method.name
        radioButton.id = ++dynamicViewId
        ViewCompat.setBackgroundTintList(
            radioButton,
            ColorStateList.valueOf(Color.parseColor(method.price))
        )

        /*if (isPreselected(method)) {
            radioButton.isChecked = true
            paymentMethodValue = method.paymentMethodSystemName
        }*/

        colorSelectionLayout.radioGridGroup.addView(radioButton)

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                colorSelectionProcess.resetRadioButton(buttonView.id)
                // paymentMethodValue = method.paymentMethodSystemName
            }
            Log.d("as", isChecked.toString())
        }

        radioButton.setOnClickListener {
            radioButton.isChecked = true
        }
    }

    private fun generateSizeSelectionRB(method: FeaturedProduct) {
        val radioButton = layoutInflater.inflate(
            R.layout.radiobutton_product_size, sizeSelectionLayout.radioGridGroup, false
        ) as AppCompatRadioButton


        radioButton.text = method.name
        radioButton.id = ++dynamicViewId

        /*if (isPreselected(method)) {
            radioButton.isChecked = true
            paymentMethodValue = method.paymentMethodSystemName
        }*/

        sizeSelectionLayout.radioGridGroup.addView(radioButton)

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sizeSelectionProcess.resetRadioButton(buttonView.id)
                // paymentMethodValue = method.paymentMethodSystemName
            }
            Log.d("as", isChecked.toString())
        }

        radioButton.setOnClickListener {
            radioButton.isChecked = true
        }
    }

    inner class TempAdapter(
        private val productsList: List<FeaturedProduct>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_featured_product, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int = productsList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyViewHolder) {
                holder.itemView.tvProductName.text = productsList[position].name
                holder.itemView.tvProductPrice.text = productsList[position].price

                Picasso.with(holder.itemView.context).load("https://picsum.photos/300/300")
                    .fit().centerInside().into(holder.itemView.ivProductThumb)
            }
        }

    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    inner class FeaturedProduct(
        val name: String,
        val price: String
    )
}