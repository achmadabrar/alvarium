package com.bs.ecommerce.auth.register

/*import android.content.Context
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
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.product.model.data.CustomerAttribute
import com.bs.ecommerce.product.model.data.ProductAttribute
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.ColorSelectionProcess
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.color_selection_layout.view.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.other_attr_layout.view.*
import org.jetbrains.anko.layoutInflater

class CustomerAttributeView(
    private val context: Context,
    private val viewModel: RegistrationViewModel,
    private val bottomSheetLayout: View,
    private val bsBehavior: BottomSheetBehavior<*>
) {
    private var attributes: List<CustomerAttribute>

    private var registrationData: GetRegistrationResponse = viewModel.getRegistrationResponseLD.value!!

    private var layoutInflater: LayoutInflater

    private val inflatedViews: MutableMap<Long, View> = mutableMapOf()

    init {
        this.attributes = registrationData.data.customerAttributes
        layoutInflater = context.layoutInflater

        for (attr in registrationData.data.customerAttributes)
        {
            genericAttributes(attr)
        }
    }

    private fun genericAttributes(attr: CustomerAttribute)
    {
        val layout = layoutInflater.inflate(R.layout.other_attr_layout, null)
        layout.tag = attr.id

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
                    val selected = viewModel.isAttrSelected(attr.id, i)

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
                                    attr.id,
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
                                viewModel.setAttrSelected(attr.id, i,
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
                            attr.id, value,
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

        inflatedViews[attr.id] = layout
    }

    fun getAttrViews(): List<View> {
        return inflatedViews.values.toMutableList()
    }

    fun onBottomSheetClose() {

    }
}*/