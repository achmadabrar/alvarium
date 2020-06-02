package com.bs.ecommerce.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.product.model.data.CustomAttribute
import com.bs.ecommerce.product.model.data.ProductPrice
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.color_selection_layout.view.*
import org.jetbrains.anko.layoutInflater
import java.util.*


class CustomAttributeManager(
    private val attributes: List<CustomAttribute>,
    private val attributeViewHolder: LinearLayout,
    private val attributeValueHolder: LinearLayout,
    private val bsBehavior: BottomSheetBehavior<*>
) {

    private val selectedAttributes = HashMap<Long, MutableList<AttributeControlValue>>()

    private lateinit var colorSelectionProcess: ColorSelectionProcess
    private lateinit var sizeSelectionProcess: ColorSelectionProcess

    private var tvProductPrice: TextView? = null
    private var priceModel: ProductPrice? = null

    private var viewGroup: ViewGroup? = null
    private var layoutInflater: LayoutInflater = attributeViewHolder.context.layoutInflater
    private val context: Context = attributeViewHolder.context

    private val inflatedViews: MutableMap<Long, View> = mutableMapOf()

    init {

        preSelectedAttributes()

        // Generate dynamic attribute views

        for (attr in attributes) {
            when (attr.attributeControlType) {

                AttributeControlType.ColorSquares -> colorSelectionAttr(attr)

                AttributeControlType.TextBox, AttributeControlType.MultilineTextbox -> textInputAttr(attr)

                AttributeControlType.Datepicker -> datePickerAttr(attr)

                else -> genericAttributes(attr)
            }
        }
    }

    // Add attribute views to parent view
    fun attachAttributesToFragment() {
        attributeViewHolder.removeAllViews()

        for (i in getAttrViews()) {
            attributeViewHolder.addView(i)
        }
    }

    private fun genericAttributes(attr: CustomAttribute) {
        val layout = layoutInflater.inflate(R.layout.custom_attribute_selection_layout, viewGroup)
        layout.tag = attr.id

        val tvName = layout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = layout.findViewById<TextView>(R.id.tvLayoutSubTitle)
        val tvSelectedAttr = layout.findViewById<TextView>(R.id.tvSelectedAttr)
        val radioGroup = RadioGroup(context)

        tvName.text = attr.name
        tvDesc.text = attr.description ?: DbHelper.getString(Const.COMMON_SELECT).plus(" ${attr.name}")
        tvSelectedAttr.text =
            attr.values.find { it.isPreSelected }?.name ?: DbHelper.getString(Const.COMMON_SELECT)

        layout.setOnClickListener {
            if (bsBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                attributeValueHolder.removeAllViews()
                radioGroup.removeAllViews()
                radioGroup.setOnCheckedChangeListener(null)

                for (i in attr.values) {
                    val label = "${i.name} ${i.priceAdjustment ?: ""}"
                    val selected = isAttrSelected(attr.id, i)

                    when (attr.attributeControlType) {
                        AttributeControlType.Checkboxes -> {
                            val cb =
                                layoutInflater.inflate(
                                    R.layout.custom_attribute_checkbox,
                                    viewGroup
                                ) as CheckBox
                            cb.text = label
                            cb.isChecked = selected
                            attributeValueHolder.addView(cb)

                            cb.setOnCheckedChangeListener { _, isChecked ->
                                setAttrSelected(
                                    attr.id,
                                    i,
                                    isChecked,
                                    true
                                )
                            }
                        }


                        AttributeControlType.ReadonlyCheckboxes -> {
                            val cb =
                                layoutInflater.inflate(
                                    R.layout.custom_attribute_checkbox,
                                    viewGroup
                                ) as CheckBox
                            cb.text = label
                            cb.isChecked = selected
                            cb.isEnabled = false
                            attributeValueHolder.addView(cb)
                        }

                        AttributeControlType.DropdownList -> {
                            val tv =
                                layoutInflater.inflate(R.layout.custom_attribute_dropdown, viewGroup) as TextView
                            tv.text = label
                            tv.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0,
                                if (selected) R.drawable.ic_tic_mark else R.drawable.transparent_tic_mark,
                                0
                            )
                            attributeValueHolder.addView(tv)

                            tv.setOnClickListener { v ->
                                setAttrSelected(
                                    attr.id, i,
                                    isSelected = true,
                                    multipleSelection = false
                                )

                                for (ii in attributeValueHolder.children) {
                                    (ii as TextView).setCompoundDrawablesWithIntrinsicBounds(
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
                                R.layout.custom_attribute_radiobutton, viewGroup
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

                        setAttrSelected(
                            attr.id, value,
                            isSelected = true,
                            multipleSelection = false
                        )
                    }

                    attributeValueHolder.addView(radioGroup)
                }

                bsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                // radioGroup.removeAllViews()
                attributeValueHolder.removeAllViews()
            }
        }

        inflatedViews[attr.id] = layout
    }

    private fun datePickerAttr(attr: CustomAttribute) {

        val layout = layoutInflater.inflate(R.layout.custom_attribute_date_picker, viewGroup)
        layout.tag = attr.id

        val tvDatePicker = layout.findViewById<TextView>(R.id.tvDatePicker)
        tvDatePicker.hint = attr.textPrompt ?: attr.name
        tvDatePicker.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, if (attr.isRequired) R.drawable.ic_star_formular else 0, 0
        )

        val calender: Calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            context, DatePickerDialog.OnDateSetListener { _, y, m, d ->
                tvDatePicker.text = TextUtils().getFormattedDate(d,m,y)

                val value = AttributeControlValue()
                value.id = -1 * attr.attributeControlType!!

                value.name = "$d-$m-$y"
                setAttrSelected(
                    attr.id, value,
                    isSelected = true,
                    multipleSelection = false
                )
            },
            calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )

        tvDatePicker.setOnClickListener{
            dialog.show()
        }

        inflatedViews[attr.id] = layout
    }

    private fun textInputAttr(attr: CustomAttribute) {
        val layout = layoutInflater.inflate(R.layout.custom_attribute_edittext, viewGroup)
        layout.tag = attr.id

        val etUserInput = layout.findViewById<EditText>(R.id.etUserInput)
        etUserInput.hint = attr.textPrompt ?: attr.name
        etUserInput.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, if (attr.isRequired) R.drawable.ic_star_formular else 0, 0
        )

        val value = AttributeControlValue()
        value.id = -1

        etUserInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                "xyz".showLog(s.toString())

                value.name = s.toString()
                setAttrSelected(attr.id, value,
                    isSelected = true,
                    multipleSelection = false
                )
            }
        })

        inflatedViews[attr.id] = layout
    }

    private fun colorSelectionAttr(attr: CustomAttribute) {
        val colorSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, viewGroup)
        colorSelectionLayout.tag = attr.id


        val tvName = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = colorSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = attr.name
        //tvDesc.text = attr.description ?: DbHelper.getString("common.select"_color)
        colorSelectionProcess = ColorSelectionProcess(colorSelectionLayout.radioGridGroup)

        for (x in attr.values) {

            val radioButton = layoutInflater.inflate(
                R.layout.radiobutton_product_color,
                colorSelectionLayout.radioGridGroup,
                false
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
                }
                setAttrSelected(attr.id, x, isChecked, false)
                Log.d("as", isChecked.toString())
            }

            radioButton.setOnClickListener {
                radioButton.isChecked = true
            }
        }

        inflatedViews[attr.id] = colorSelectionLayout
    }

    private fun sizeSelectionAttr(attr: CustomAttribute) {
        val sizeSelectionLayout = layoutInflater.inflate(R.layout.color_selection_layout, viewGroup)
        sizeSelectionLayout.tag = attr.id

        val tvName = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutTitle)
        val tvDesc = sizeSelectionLayout.findViewById<TextView>(R.id.tvLayoutSubTitle)

        tvName.text = attr.name
        //tvDesc.text = attr.description ?: DbHelper.getString("common.select")
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
                setAttrSelected(attr.id, x, isChecked, false)
            }

            radioButton.setOnClickListener {
                radioButton.isChecked = true
            }
        }


        inflatedViews[attr.id] = sizeSelectionLayout
    }

    private fun getAttrViews(): List<View> {
        return inflatedViews.values.toMutableList()
    }

    private fun preSelectedAttributes() {
        // sorting attribute values
        for (attr in attributes) {
            attr.values = attr.values.sortedBy { !it.isPreSelected }

            val list = mutableListOf<AttributeControlValue>()

            for (value in attr.values) {
                if (value.isPreSelected) {
                    list.add(value)

                    if (attr.attributeControlType != AttributeControlType.Checkboxes)
                        break
                }
            }

             selectedAttributes[attr.id] = list
        }
    }

    private fun isAttrSelected(attrId: Long?, value: AttributeControlValue): Boolean {
        return selectedAttributes[attrId]?.contains(value) ?: false
    }

    private fun setAttrSelected(
        attrId: Long?, value: AttributeControlValue,
        isSelected: Boolean, multipleSelection: Boolean
    ) {

        if (isSelected) {
            if (!multipleSelection) selectedAttributes[attrId!!] = mutableListOf()

            selectedAttributes[attrId]?.add(value)
        } else
            selectedAttributes[attrId]?.remove(value)

        updateUI()
        adjustProductPrice()
    }

    private fun updateUI() {
        for (i in selectedAttributes.keys) {
            val view = attributeViewHolder.findViewWithTag<View>(i)
            val textView = view?.findViewById<TextView>(R.id.tvSelectedAttr)

            val selectedAttr = selectedAttributes[i]

            if (selectedAttr.isNullOrEmpty()) {
                textView?.text = DbHelper.getString(Const.COMMON_SELECT)
            } else {
                textView?.text = selectedAttributes[i]?.get(0)?.name
            }
        }
    }

    fun setupProductPriceCalculation(priceModel: ProductPrice?, textView: TextView) {

        if(priceModel == null) return

        this.priceModel = priceModel
        tvProductPrice = textView

        adjustProductPrice()
    }

    private fun adjustProductPrice() {

        tvProductPrice?.apply {

            if(priceModel!!.isRental == true) {
                text = priceModel!!.rentalPrice
                return@apply
            }

            var price = priceModel!!.priceValue!!

            for (valueList in selectedAttributes.values) {
                for (i in valueList) {
                    price = price.plus(i.priceAdjustmentValue ?: 0.0)
                }
            }

            text = "$%.2f".format(price)
        }
    }


    fun getFormData(productAttributePrefix: String): KeyValueFormData {

        val allKeyValueList = ArrayList<KeyValuePair>()

        for ((key, valueList) in selectedAttributes)
        {
            if(valueList.isNotEmpty())
            {
                for(attribute in valueList)
                {

                    if(attribute.id == -1 * AttributeControlType.Datepicker) {
                        val times = attribute.name?.splitToSequence("-")?.filter { it.isNotEmpty() }?.toList()

                        val day = times?.get(0) ?: "1"
                        val month = times?.get(1) ?: "1"
                        val year = times?.get(2) ?: "1970"

                        allKeyValueList.add(KeyValuePair("${productAttributePrefix}_${key}_day", day))
                        allKeyValueList.add(KeyValuePair("${productAttributePrefix}_${key}_month", month))
                        allKeyValueList.add(KeyValuePair("${productAttributePrefix}_${key}_year", year))

                    } else if (attribute.id == -1) { // -1 for textAttr & -2 for datePickerAttr
                        allKeyValueList.add(KeyValuePair("${productAttributePrefix}_${key}", attribute.name ?: ""))

                    } else {
                        val keyValuePair = KeyValuePair()

                        keyValuePair.key = "${productAttributePrefix}_${key}"
                        keyValuePair.value = attribute.id.toString()
                        allKeyValueList.add(keyValuePair)
                    }

                }
            }
        }

        return KeyValueFormData( allKeyValueList )
    }
}