package com.bs.ecommerce.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.data.SecondSubcategory
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.loadImg

class SecondLevelAdapter(
    private val context: Context,
    private val subCatList: List<Subcategory>,
    private val itemClickListener: ItemClickListener<LeftDrawerItem>
) :
    BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Subcategory {
        return subCatList[groupPosition]
    }

    override fun getGroupCount(): Int {
        return subCatList.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return subCatList[groupPosition].categoryId.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var mConvertView = convertView
        if (mConvertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mConvertView = inflater.inflate(R.layout.item_category_tree_level_2, null)
            val text =
                mConvertView!!.findViewById(R.id.tvName) as TextView
            text.text = subCatList[groupPosition].name

            text.setOnClickListener {
                itemClickListener.onClick(
                    mConvertView, groupPosition, LeftDrawerItem(
                        subCatList[groupPosition].categoryId,
                        subCatList[groupPosition].name,
                        isCategory = false
                    )
                )
            }

            val thumb = mConvertView.findViewById(R.id.ivThumb) as ImageView
            thumb.loadImg(subCatList[groupPosition].iconUrl)

            val icon = mConvertView.findViewById(R.id.ivExpand) as ImageView

            if (isExpanded) {
                icon.setImageResource(R.drawable.ic_minus)
            } else {
                icon.setImageResource(R.drawable.ic_plus)
            }

            icon.visibility = if (subCatList[groupPosition].subcategories.isNullOrEmpty())
                View.GONE else View.VISIBLE
        }
        return mConvertView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): SecondSubcategory {
        return subCatList[groupPosition].subcategories[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return subCatList[groupPosition].subcategories[childPosition].categoryId.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var mConvertView = convertView
        if (mConvertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mConvertView = inflater.inflate(R.layout.item_category_tree_level_3, null)
            val text =
                mConvertView!!.findViewById(R.id.tvName) as TextView
            text.text = subCatList[groupPosition].subcategories[childPosition].name


            text.setOnClickListener {
                itemClickListener.onClick(
                    mConvertView, groupPosition, LeftDrawerItem(
                        subCatList[groupPosition].subcategories[childPosition].categoryId,
                        subCatList[groupPosition].subcategories[childPosition].name,
                        isCategory = false
                    )
                )
            }
        }
        return mConvertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return subCatList[groupPosition].subcategories.size
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(
        groupPosition: Int,
        childPosition: Int
    ): Boolean {
        return true
    }

}