package com.bs.ecommerce.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.loadImg

class FirstLevelAdapter(
    private val context: Context,
    private val categoryList: List<Category>,
    private val itemClickListener: ItemClickListener<LeftDrawerItem>
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): Subcategory {
        return categoryList[groupPosition].subcategories[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return try {
            categoryList[groupPosition].subcategories[childPosition].categoryId.toLong()
        } catch (e: Exception) {
            0
        }
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val secondLevelELV = SecondLevelExpandableListView(context)
        val adapter = SecondLevelAdapter(
            context,
            categoryList[groupPosition].subcategories,
            itemClickListener
        )

        secondLevelELV.setAdapter(adapter)
        secondLevelELV.setGroupIndicator(null)
        secondLevelELV.divider = null
        secondLevelELV.setChildDivider(null)

        secondLevelELV.setOnGroupClickListener { p, v, gp, _ ->

            val groupIndicator = v.findViewById(R.id.ivExpand) as ImageView

            if (p.isGroupExpanded(gp)) {
                p.collapseGroup(gp)
                groupIndicator.setImageResource(R.drawable.ic_plus)
            } else {
                p.expandGroup(gp)
                groupIndicator.setImageResource(R.drawable.ic_minus)
            }

            adapter.notifyDataSetChanged()
            true
        }

        return secondLevelELV
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupPosition
    }

    override fun getGroupCount(): Int {
        return categoryList.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
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
            mConvertView = inflater.inflate(R.layout.item_category_tree_level_1, null)

            val text = mConvertView?.findViewById(R.id.tvName) as TextView
            text.text = categoryList[groupPosition].name

            text.setOnClickListener {
                itemClickListener.onClick(
                    mConvertView, groupPosition, LeftDrawerItem(
                        categoryList[groupPosition].categoryId,
                        categoryList[groupPosition].name,
                        isCategory = true
                    )
                )
            }

            val thumb = mConvertView.findViewById(R.id.ivThumb) as ImageView
            thumb.loadImg(categoryList[groupPosition].iconUrl)

            val icon = mConvertView.findViewById(R.id.ivExpand) as ImageView

            if (isExpanded) {
                icon.setImageResource(R.drawable.ic_minus)
            } else {
                icon.setImageResource(R.drawable.ic_plus)
            }

            icon.visibility = if (categoryList[groupPosition].subcategories.isNullOrEmpty())
                View.GONE else View.VISIBLE
        }

        return mConvertView
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