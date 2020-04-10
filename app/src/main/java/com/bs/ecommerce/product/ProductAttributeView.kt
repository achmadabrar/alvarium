package com.bs.ecommerce.product

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.ViewCompat
import com.bs.ecommerce.R
import com.bs.ecommerce.product.data.ProductAttribute
import com.bs.ecommerce.product.data.ProductDetail
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.ColorSelectionProcess
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.color_selection_layout.view.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import kotlinx.android.synthetic.main.other_attr_layout.view.*
import org.jetbrains.anko.layoutInflater

class ProductAttributeView (
    private val context: Context,
    product: ProductDetail,
    private val bottomSheetLayout: View,
    private val bsBehavior: BottomSheetBehavior<*>
){
    private var attributes: List<ProductAttribute>
    private lateinit var colorSelectionProcess: ColorSelectionProcess
    private lateinit var sizeSelectionProcess: ColorSelectionProcess
    private var layoutInflater: LayoutInflater


    val inflatedViews: MutableList<View> = mutableListOf()

    init {
        this.attributes = product.productAttributes
        layoutInflater = context.layoutInflater

        for(attr in product.productAttributes) {
            when (attr.attributeControlType) {

                AttributeControlType.ColorSquares -> colorSelectionAttr(attr)

                // AttributeControlType.TextBox -> sizeSelectionAttr(attr)

                else -> otherAttrs(attr)
            }
        }
    }

    private fun otherAttrs(attr: ProductAttribute) {
        val layout = layoutInflater.inflate(R.layout.other_attr_layout, null)

        val tvName = layout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = layout.findViewById<TextView>(R.id.tvLayoutSubTitle)
        val tvSelectedAttr = layout.findViewById<TextView>(R.id.tvSelectedAttr)
        val radioGroup = RadioGroup(context)

        tvName.text = attr.name
        tvDesc.text = attr.description ?: "Select your ${attr.name}"
        tvSelectedAttr.text = attr.values.find { it.isPreSelected }?.name ?: "Select"

        layout.tvSelectedAttr.setOnClickListener {
            if (bsBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetLayout.attributeValueHolder.removeAllViews()

                for (i in attr.values) {
                    when (attr.attributeControlType) {
                        AttributeControlType.Checkboxes -> {
                            val cb =
                                layoutInflater.inflate(R.layout.other_attr_checkbox, null) as CheckBox
                            cb.text = i.name
                            cb.isChecked = i.isPreSelected
                            bottomSheetLayout.attributeValueHolder.addView(cb)
                        }

                        AttributeControlType.DropdownList -> {
                            val cb =
                                layoutInflater.inflate(R.layout.other_attr_item, null) as TextView
                            cb.text = i.name
                            //cb.isChecked = i.isPreSelected
                            bottomSheetLayout.attributeValueHolder.addView(cb)
                        }

                        AttributeControlType.RadioList -> {
                            //radioGroup.visibility = View.VISIBLE

                            val rb =
                                layoutInflater.inflate(R.layout.other_attr_radiobutton, null) as RadioButton
                            rb.text = i.name
                            //rb.isSelected = i.isPreSelected

                            radioGroup.addView(rb)
                        }
                    }
                }

                if(attr.attributeControlType == AttributeControlType.RadioList)
                    bottomSheetLayout.attributeValueHolder.addView(radioGroup)

                bsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                radioGroup.removeAllViews()
                bottomSheetLayout.attributeValueHolder.removeAllViews()
            }
        }

        inflatedViews.add(layout)
    }

    private fun colorSelectionAttr(colorAttr: ProductAttribute) {
        val colorSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, null)

        val tvName = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = colorAttr.name
        tvDesc.text = colorAttr.description ?: context.getString(R.string.select_color)
        colorSelectionProcess = ColorSelectionProcess(colorSelectionLayout.radioGridGroup)

        for (x in colorAttr.values) {

            val radioButton = layoutInflater.inflate(
                R.layout.radiobutton_product_color, colorSelectionLayout.radioGridGroup, false
            ) as AppCompatRadioButton

            radioButton.id = x.id //++dynamicViewId
            radioButton.isChecked = x.isPreSelected
            ViewCompat.setBackgroundTintList(
                radioButton,
                ColorStateList.valueOf(Color.parseColor(x.colorSquaresRgb ?: "#000"))
            )

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

        inflatedViews.add(colorSelectionLayout)
    }

    private fun sizeSelectionAttr(colorAttr: ProductAttribute) {
        val sizeSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, null)

        val tvName = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = colorAttr.name
        tvDesc.text = colorAttr.description ?: context.getString(R.string.select_size)
        sizeSelectionProcess = ColorSelectionProcess(sizeSelectionLayout.radioGridGroup)

        for (x in colorAttr.values) {

            val radioButton = layoutInflater.inflate(
                R.layout.radiobutton_product_size, sizeSelectionLayout.radioGridGroup, false
            ) as AppCompatRadioButton

            radioButton.id = x.id
            radioButton.isChecked = x.isPreSelected

            sizeSelectionLayout.radioGridGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    sizeSelectionProcess.resetRadioButton(buttonView.id)
                }
                Log.d("as", isChecked.toString())
            }

            radioButton.setOnClickListener {
                radioButton.isChecked = true
            }
        }

        inflatedViews.add(sizeSelectionLayout)
    }

    public fun getAttrViews() : List<View> {
        return inflatedViews
    }
}