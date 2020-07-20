package com.bs.ecommerce.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bs.ecommerce.R
import com.bs.ecommerce.customViews.CustomExpandableListView
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.loadImg

/**
 * Created by bs206 on 3/16/18.
 */
class CategoryListAdapterExpandable(
    private val categories: List<Category>,
    private val itemClickListener: ItemClickListener<LeftDrawerItem>
)
    : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): List<Subcategory> = categories[groupPosition].subcategories

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup): View {

        val subCategory = getChild(groupPosition, childPosition)
        var convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_expandable_list_child_subcategory, parent, false)

        var customList = convertView.findViewById<View>(R.id.expandList) as CustomExpandableListView
        customList.setAdapter(SubCategoryListAdapter(subCategory, itemClickListener))
        customList.setGroupIndicator(null)

        // code for collapse all group except selected one
        var lastExpandedPosition = -1
        customList.setOnGroupExpandListener{ groupPosition ->
            if (lastExpandedPosition !== -1 && groupPosition != lastExpandedPosition) {
                customList.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        }


        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int = 1


    override fun getGroup(groupPosition: Int): Category = categories[groupPosition]


    override fun getGroupCount(): Int = categories.size


    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View
    {
        var convertView = convertView

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.item_expandable_list_group_category, null)
        val logo = convertView.findViewById<View>(R.id.logo) as AppCompatImageView
        val expandableIcon = convertView.findViewById<View>(R.id.topCategoryExpandableButton) as ImageView
        val text = convertView.findViewById<View>(R.id.topCategoryName) as TextView

        val subCategory = getGroup(groupPosition)
        text.text = subCategory.name

        if (getChildrenCountForHidingGroupIndicator(groupPosition) < 1)
        {
            expandableIcon.visibility = View.INVISIBLE
            convertView.setOnClickListener{
                itemClickListener.onClick(
                    convertView, groupPosition, LeftDrawerItem(
                        subCategory.categoryId, subCategory.name, isCategory = true
                    )
                )
            }
        }

        else
        {
            expandableIcon.visibility = View.VISIBLE
            if (isExpanded)
                expandableIcon.setImageResource(R.drawable.ic_minus_left_category)
            else
                expandableIcon.setImageResource(R.drawable.ic_plus_left_category)
        }

        text.setOnClickListener{
            itemClickListener.onClick(
                text, groupPosition, LeftDrawerItem(
                    subCategory.categoryId, subCategory.name, isCategory = true
                )
            )
        }

        logo.loadImg(subCategory.iconUrl, roundedCorner = false)

        return convertView
    }

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    private fun getChildrenCountForHidingGroupIndicator(groupPosition: Int): Int = categories[groupPosition].subcategories.size
}