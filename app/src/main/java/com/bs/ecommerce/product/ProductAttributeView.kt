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
import androidx.core.view.children
import com.bs.ecommerce.R
import com.bs.ecommerce.product.data.AttributeControlValue
import com.bs.ecommerce.product.data.ProductAttribute
import com.bs.ecommerce.product.data.ProductDetail
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.ColorSelectionProcess
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.color_selection_layout.view.*
import kotlinx.android.synthetic.main.other_attr_bottom_sheet.view.*
import kotlinx.android.synthetic.main.other_attr_layout.view.*
import org.jetbrains.anko.layoutInflater

class ProductAttributeView(
    private val context: Context,
    private val viewModel: ProductDetailViewModel,
    private val bottomSheetLayout: View,
    private val bsBehavior: BottomSheetBehavior<*>
) {
    private var attributes: List<ProductAttribute>
    private lateinit var colorSelectionProcess: ColorSelectionProcess
    private lateinit var sizeSelectionProcess: ColorSelectionProcess
    private var product: ProductDetail = viewModel.productLiveData.value!!
    private var layoutInflater: LayoutInflater

    private val inflatedViews: MutableMap<Long, View> = mutableMapOf()

    init {
        this.attributes = product.productAttributes
        layoutInflater = context.layoutInflater

        for (attr in product.productAttributes) {
            when (attr.attributeControlType) {

                AttributeControlType.ColorSquares -> colorSelectionAttr(attr)

                // AttributeControlType.TextBox -> sizeSelectionAttr(attr)

                else -> genericAttributes(attr)
            }
        }
    }

    private fun genericAttributes(attr: ProductAttribute) {
        val layout = layoutInflater.inflate(R.layout.other_attr_layout, null)
        layout.tag = attr.productAttributeId

        val tvName = layout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = layout.findViewById<TextView>(R.id.tvLayoutSubTitle)
        val tvSelectedAttr = layout.findViewById<TextView>(R.id.tvSelectedAttr)
        val radioGroup = RadioGroup(context)

        tvName.text = attr.name
        tvDesc.text = attr.description ?: "Select your ${attr.name}"
        tvSelectedAttr.text =
            attr.values.find { it.isPreSelected }?.name ?: context.getString(R.string.select)

        layout.tvSelectedAttr.setOnClickListener {
            if (bsBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetLayout.attributeValueHolder.removeAllViews()
                radioGroup.removeAllViews()
                radioGroup.setOnCheckedChangeListener(null)

                for (i in attr.values) {
                    val label = "${i.name} ${i.priceAdjustment ?: ""}"
                    val selected = viewModel.isAttrSelected(attr.productAttributeId, i)

                    when (attr.attributeControlType) {
                        AttributeControlType.Checkboxes -> {
                            val cb =
                                layoutInflater.inflate(
                                    R.layout.other_attr_checkbox,
                                    null
                                ) as CheckBox
                            cb.text = label
                            cb.isChecked = selected
                            bottomSheetLayout.attributeValueHolder.addView(cb)

                            cb.setOnCheckedChangeListener { _, isChecked ->
                                viewModel.setAttrSelected(
                                    attr.productAttributeId,
                                    i,
                                    isChecked,
                                    true
                                )
                            }
                        }

                        AttributeControlType.DropdownList -> {
                            val tv =
                                layoutInflater.inflate(R.layout.generic_attr_item, null) as TextView
                            tv.text = label
                            tv.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0,
                                if (selected) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                                0
                            )
                            bottomSheetLayout.attributeValueHolder.addView(tv)

                            tv.setOnClickListener { v ->
                                viewModel.setAttrSelected(attr.productAttributeId, i,
                                    isSelected = true,
                                    multipleSelection = false
                                )

                                for (i in bottomSheetLayout.attributeValueHolder.children) {
                                    (i as TextView).setCompoundDrawablesWithIntrinsicBounds(
                                        0, 0, R.drawable.transparent_tic_mark, 0
                                    )
                                    (v as TextView).setCompoundDrawablesWithIntrinsicBounds(
                                        0, 0, R.drawable.ic_tic_mark, 0
                                    )
                                }
                            }
                        }

                        AttributeControlType.RadioList -> {

                            val rb = layoutInflater.inflate(
                                R.layout.other_attr_radiobutton, null
                            ) as RadioButton
                            rb.text = label
                            rb.tag = i

                            radioGroup.addView(rb)
                            rb.isChecked = selected
                        }
                    }
                }

                if (attr.attributeControlType == AttributeControlType.RadioList) {
                    radioGroup.setOnCheckedChangeListener { v, checkedId ->
                        val rb: RadioButton =
                            v.findViewById(checkedId) ?: return@setOnCheckedChangeListener
                        val value = rb.tag as AttributeControlValue

                        viewModel.setAttrSelected(
                            attr.productAttributeId, value,
                            isSelected = true,
                            multipleSelection = false
                        )
                    }

                    bottomSheetLayout.attributeValueHolder.addView(radioGroup)
                }

                bsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                // radioGroup.removeAllViews()
                bottomSheetLayout.attributeValueHolder.removeAllViews()
            }
        }

        inflatedViews[attr.productAttributeId] = layout
    }

    private fun colorSelectionAttr(attr: ProductAttribute) {
        val colorSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, null)
        colorSelectionLayout.tag = attr.productAttributeId


        val tvName = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = attr.name
        tvDesc.text = attr.description ?: context.getString(R.string.select_color)
        colorSelectionProcess = ColorSelectionProcess(colorSelectionLayout.radioGridGroup)

        for (x in attr.values) {

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
                viewModel.setAttrSelected(attr.productAttributeId, x, isChecked, false)
                Log.d("as", isChecked.toString())
            }

            radioButton.setOnClickListener {
                radioButton.isChecked = true
            }
        }

        inflatedViews[attr.productAttributeId] = colorSelectionLayout
    }

    private fun sizeSelectionAttr(attr: ProductAttribute) {
        val sizeSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, null)
        sizeSelectionLayout.tag = attr.productAttributeId

        val tvName = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = attr.name
        tvDesc.text = attr.description ?: context.getString(R.string.select_size)
        sizeSelectionProcess = ColorSelectionProcess(sizeSelectionLayout.radioGridGroup)

        for (x in attr.values) {

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
                viewModel.setAttrSelected(attr.productAttributeId, x, isChecked, false)
            }

            radioButton.setOnClickListener {
                radioButton.isChecked = true
            }
        }


        inflatedViews[attr.productAttributeId] = sizeSelectionLayout
    }

    fun getAttrViews(): List<View> {
        return inflatedViews.values.toMutableList()
    }

    fun onBottomSheetClose() {

    }
}