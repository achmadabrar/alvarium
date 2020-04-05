package com.bs.ecommerce.customViews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.data.AttributeControlValue
import com.bs.ecommerce.auth.data.KeyValuePair
import com.bs.ecommerce.auth.data.ProductAttribute
import com.bs.ecommerce.utils.AttributeControlType
import com.google.android.material.appbar.AppBarLayout
import java.util.*


open class ProductAttributeViews : LinearLayout {
    lateinit var layout: LinearLayout
    lateinit var attributes: List<ProductAttribute>
    lateinit var thisContext: Context
    lateinit var valueTextPairMap: MutableMap<String, String>
    lateinit var editTextList: MutableMap<ProductAttribute, EditText>
    val productAttributePrefix = "product_attribute"
    lateinit var attributeLayout: LinearLayout


    var currentFragment : Fragment? = null


    private val gridView: GridView
        get() = layoutInflater.inflate(R.layout.gridview, null) as GridView

    private val layoutInflater: LayoutInflater
        get() = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    val productAttribute: MutableList<KeyValuePair>
        get() {
            val keyValuePairs = ArrayList<KeyValuePair>()
            for (value in valueTextPairMap.keys) {
                val keyValuePair = KeyValuePair()
                keyValuePair.key = valueTextPairMap[value].toString()
                keyValuePair.value = value
                keyValuePairs.add(keyValuePair)
                Log.v("key,Value:", valueTextPairMap[value] + "," + value)
            }
            return keyValuePairs
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attributes: List<ProductAttribute>, layout: LinearLayout,  currentFragment: Fragment) : super(context) {
        this.attributes = attributes
        this.layout = layout
        this.thisContext = context
        editTextList = HashMap()
        valueTextPairMap = HashMap()

        this.currentFragment = currentFragment

        generateView()

        Handler().postDelayed({ callPriceWebservice() }, 1000)
    }

    private fun removeAllExistingViews() {
        layout.removeAllViews()
    }

    private fun generateView()
    {
        removeAllExistingViews()

        for (productAttribute in attributes)
        {
            generateattributeLayout()

            generateViewLabel(productAttribute.name, productAttribute)


            when (productAttribute.attributeControlType)
            {
                AttributeControlType.DropdownList -> generateDropdownList(productAttribute)

                AttributeControlType.TextBox -> generateEdittext(productAttribute)

                AttributeControlType.RadioList -> generateRadioGroup(productAttribute)

                AttributeControlType.Checkboxes -> generateCheckBoxContainerGridlayout(productAttribute)

                AttributeControlType.ReadonlyCheckboxes -> generateDiasbaleCheckbox(productAttribute)

                AttributeControlType.ColorSquares -> generateColorSqauares(productAttribute)

                AttributeControlType.MultilineTextbox -> generateEdittext(productAttribute)
            }
        }
    }


    private fun generateEdittext(productAttribute: ProductAttribute) {
        val editText = layoutInflater.inflate(R.layout.attribute_textbox, null) as EditText

        productAttribute.defaultValue?.let { editText.hint = productAttribute.defaultValue }

        editTextList.put(productAttribute, editText)
        addViewintLayout(editText)

    }

    private fun generateRadioGroup(productAttribute: ProductAttribute)
    {
       /* val radioGridGroup = layoutInflater.inflate(R.layout.custom_flow_type_radio_group, null) as FlowMaterialRadioGroup
        addButtoninRadioGroup(productAttribute, radioGridGroup)

        addViewintLayout(radioGridGroup)*/

    }

    private fun generateColorSqauares(productAttribute: ProductAttribute)
    {
        val linearLayout = layoutInflater.inflate(R.layout.attribute_linear_layout, null) as LinearLayout



        for (value in productAttribute.values!!) {
            //CheckableImageButton checkableImageButton = generateCheckableImageButton(value);

            val imageViewParams = AppBarLayout.LayoutParams(80, 80)
            imageViewParams.setMargins(0, 0, 10, 0)

            val imageView = ImageView(context)
            imageView.layoutParams = imageViewParams
            imageView.maxHeight = 20
            imageView.maxWidth = 20

            val shapeDrawable: GradientDrawable
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shapeDrawable = resources.getDrawable(R.drawable.attribute_bg_color_square, context.theme) as GradientDrawable
            } else {
                shapeDrawable = resources.getDrawable(R.drawable.attribute_bg_color_square) as GradientDrawable
            }

            try {
                shapeDrawable.setColor(Color.parseColor(value.colorSquaresRgb))
                shapeDrawable.setStroke(5, Color.TRANSPARENT, 10f, 10f)
            } catch (ex: Exception) {
                // who cares
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.background = shapeDrawable
            } else {
                imageView.setBackgroundDrawable(shapeDrawable)
            }

            imageView.isClickable = true
            imageView.isFocusable = true
            linearLayout.addView(imageView)

            if (value.isPreSelected) {
                selectColorSquare(imageView, value)
                updateHashmapWithdefaultValue(productAttribute, value, true)
            }

            /*Picasso.with(context).load(value.pictureModel!!.imageUrl)
                    .fit().centerInside()
                    .into(imageView)*/

            imageView.setOnClickListener { v ->
                hasElementInyMapofSingleChoice(productAttribute)
                updateDataInHashMap(productAttribute, value, true)
                clearSelection(linearLayout)
                selectColorSquare(v, value)
            }
        }

        addViewintLayout(linearLayout)
    }

    private fun selectColorSquare(view: View, value: AttributeControlValue) {
        val shapeDrawable: GradientDrawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shapeDrawable = resources.getDrawable(R.drawable.attribute_bg_color_square, context.theme) as GradientDrawable
        } else {
            shapeDrawable = resources.getDrawable(R.drawable.attribute_bg_color_square) as GradientDrawable
        }

        try {
            shapeDrawable.setColor(Color.parseColor(value.colorSquaresRgb))
            shapeDrawable.setStroke(5, Color.parseColor(value.colorSquaresRgb), 10f, 10f)
        } catch (ex: Exception) {
            // who cares
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = shapeDrawable
        } else {
            view.setBackgroundDrawable(shapeDrawable)
        }
    }

    private fun clearSelection(layout: LinearLayout) {
        for (i in 0 until layout.childCount) {
            val imageView = layout.getChildAt(i) as ImageView

            val shapeDrawable = imageView.background as GradientDrawable
            try {
                shapeDrawable.setStroke(5, Color.TRANSPARENT, 10f, 10f)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.background = shapeDrawable
            } else {
                imageView.setBackgroundDrawable(shapeDrawable)
            }
        }
    }

   /* private fun addButtoninRadioGroup(productAttribute: ProductAttribute, radioGridGroup: FlowMaterialRadioGroup) {
        for (value in productAttribute.values!!) {
            val radioButton = generateRadioButton(value)
            if (isPreselected(value)) {
                radioButton.post { radioButton.isChecked = true }

                updateHashmapWithdefaultValue(productAttribute, value, true)
            }
            radioGridGroup.addView(radioButton)
        }

        radioGridGroup.setOnCheckedChangeListener(object : FlowMaterialRadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: FlowMaterialRadioGroup, checkedId: Int) {
                val value = getAttributeControlValue(checkedId, productAttribute.values)
                hasElementInyMapofSingleChoice(productAttribute)

                updateDataInHashMap(productAttribute, value, true)
            }
        })

    }*/

    private fun generateCheckBoxContainerGridlayout(productAttribute: ProductAttribute) {
        val gridLayout = generateGridLayout()
        addCheckBoxInGridLayout(productAttribute, gridLayout)
        addViewintLayout(gridLayout)
        /* GridView  gridView=getGridView();
            CheckBoxAttributeGridAdapter adapter=new CheckBoxAttributeGridAdapter(context,R.layout.checkbox,values);
            gridView.setAdapter(adapter);
            addViewintLayout(gridView);*/

    }


    private fun generateDiasbaleCheckbox(values: ProductAttribute) {
        val gridLayout = generateGridLayout()
        addCheckBoxInGridLayout(values, gridLayout)
        makeCheckboxReadOnly(gridLayout)
        addViewintLayout(gridLayout)

    }


    private fun generateDropdownList(productAttribute: ProductAttribute) {

        if (productAttribute.values.isNotEmpty())
        {
            val spinner = AppCompatSpinner(context)
            val adapter = ArrayAdapter(context, R.layout.attribute_simple_spinner_item_black_color, getDropDownListData(productAttribute.values))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val preSelectedPosition = getPreSelectedposition(productAttribute)


            spinner.adapter = adapter
            addViewintLayout(spinner)
            spinner.post { spinner.setSelection(preSelectedPosition) }


            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {


                    val value = productAttribute.values[position]
                    hasElementInyMapofSingleChoice(productAttribute)
                    updateDataInHashMap(productAttribute, value, true)

                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        }
        else
        {
            val key = getKey(productAttribute)
            valueTextPairMap.put("" + productAttribute.id, key)
        }
    }

    private fun getPreSelectedposition(productAttribute: ProductAttribute): Int {
        var index = -1
        var position = -1
        for (value in productAttribute.values!!) {
            ++index
            if (isPreselected(value)) {
                position = index
                break
            }
        }

        return position
    }

    private fun isPreselected(value: AttributeControlValue): Boolean {
        return (value.isPreSelected)
    }

    private fun getDropDownListData(values: List<AttributeControlValue>?): ArrayList<String> {
        val dataList = ArrayList<String>()
        for (value in values!!) {
            dataList.add(value.name!! + getAtributeValueText(value))
        }
        return dataList

    }

    private fun getAtributeValueText(value: AttributeControlValue): String {
        val text: String
        if (value.priceAdjustment != null && !value.priceAdjustment!!.isEmpty())
            text = "[" + value.priceAdjustment + "]"
        else
            text = ""
        return text

    }

/*    private fun generateRadioButton(value: AttributeControlValue): RadioButton {
        val radioButton = layoutInflater.inflate(R.layout.radiobutton, null) as RadioButton

        radioButton.text = value.name!! + getAtributeValueText(value)

        radioButton.id = value.id
        return radioButton
    }*/

    /*private CheckableImageButton generateCheckableImageButton(AttributeControlValue value) {
        CheckableImageButton checkableImageButton = getLayoutInflater().inflate(R.layout.checkable_image_button, null);
        return  checkableImageButton;
    }*/

    private fun generateCheckBox(productAttribute: ProductAttribute, value: AttributeControlValue): CheckBox {
        val checkBox = layoutInflater.inflate(R.layout.attribute_textbox, null) as CheckBox
        checkBox.text = value.name!! + getAtributeValueText(value)
        checkBox.id = value.id
        if (isPreselected(value)) {
            checkBox.post { checkBox.isChecked = true }

            updateHashmapWithdefaultValue(productAttribute, value, true)
        }
        checkBox.setOnCheckedChangeListener { buttonView, isChecked -> updateDataInHashMap(productAttribute, value, isChecked) }
        return checkBox
    }


    private fun generateGridLayout(): androidx.gridlayout.widget.GridLayout {
        return layoutInflater.inflate(R.layout.attribute_gridlayout, null) as androidx.gridlayout.widget.GridLayout

    }


    private fun addCheckBoxInGridLayout(productAttribute: ProductAttribute, gridLayout: androidx.gridlayout.widget.GridLayout) {
        for (value in productAttribute.values) {
            gridLayout.addView(generateCheckBox(productAttribute, value))
        }
    }

    private fun makeCheckboxReadOnly(gridLayout: androidx.gridlayout.widget.GridLayout) {
        for (index in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(index) as CheckBox
            child.isChecked = true
            child.isEnabled = false

        }
    }

    private fun generateViewLabel(label: String?, productAttribute: ProductAttribute) {
        val textView = layoutInflater.inflate(R.layout.attribute_text_view, layout, false) as TextView
        textView.text = label
        if (productAttribute.isRequired)
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_formular, 0)
        attributeLayout.addView(textView)
        // addViewintLayout(textView);
    }

    private fun generateattributeLayout() {
        attributeLayout = layoutInflater.inflate(R.layout.attribute_separate_layout_each_attribute_product_details, layout, false) as LinearLayout

    }

    private fun addViewintLayout(view: View) {
        attributeLayout.addView(view)
        layout.addView(attributeLayout)
    }


    open fun updateDataInHashMap(productAttribute: ProductAttribute, value: AttributeControlValue?, ischecked: Boolean) {
        updateHashmapWithdefaultValue(productAttribute, value, ischecked)

        callPriceWebservice()
    }

    open fun updateHashmapWithdefaultValue(productAttribute: ProductAttribute, value: AttributeControlValue?, ischecked: Boolean) {
        if (ischecked) {
            val key = getKey(productAttribute)
            valueTextPairMap.put("" + value!!.id, key)
            //    Log.v("MMMMMkey,Value:",key+","+value.getId());

        } else
            valueTextPairMap.remove("" + value!!.id)


    }

    fun putEdittextValueInMap() {
        for (productAttribute in editTextList.keys) {
            hasElementInyMapofSingleChoice(productAttribute)
            val key = getKey(productAttribute)

            if (editTextList[productAttribute]?.text.toString().isNotEmpty()) {
                valueTextPairMap.put("" + editTextList[productAttribute]?.text.toString() as CharSequence, key)
            } else {
                if (editTextList[productAttribute]?.text.toString().isEmpty() && productAttribute.defaultValue != null)
                    valueTextPairMap.put("" + productAttribute.defaultValue as CharSequence, key)
                else if (editTextList[productAttribute]?.text.toString().isEmpty() && productAttribute.defaultValue == null)
                    valueTextPairMap.put("", key)
            }
        }
    }

    open fun getKey(productAttribute: ProductAttribute): String {
        //return String.format("%s_%d_%d_%d", productAttributePrefix, productAttribute.productId, productAttribute.productAttributeId, productAttribute.id)
        return "${productAttributePrefix}_${productAttribute.productId}_${productAttribute.productAttributeId}_${productAttribute.id}"
    }


    open fun callPriceWebservice()
    {
        //ProductDetailFragment.productModel?.id?.let { currentFragment?.view?.let {  RetroClient.api.getUpdatedPrice(ProductDetailFragment.productModel?.id!!, productAttribute).enqueue(CustomCB(it))  } }
    }

    private fun removeDataInHashMap(desiredValue: String) {
        for ((key, value) in valueTextPairMap)
            if (value == desiredValue) {
                valueTextPairMap.remove(key)
                break
            }


    }

    private fun hasElementInyMapofSingleChoice(productAttribute: ProductAttribute) {
        val key = getKey(productAttribute)
        if (valueTextPairMap.containsValue(key))
            removeDataInHashMap(key)
    }

    fun getAttributeControlValue(id: Int, values: List<AttributeControlValue>?): AttributeControlValue? {
        for (value in values!!) {
            if (value.id == id)
                return value
        }
        return null

    }


}
