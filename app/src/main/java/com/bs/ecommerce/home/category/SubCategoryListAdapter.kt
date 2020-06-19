package com.bs.ecommerce.home.category

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.data.SecondSubcategory
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.loadImg


/**
 * Created by bs206 on 3/16/18.
 */
class SubCategoryListAdapter(
    private val categories: List<Subcategory>,
    private val itemClickListener: ItemClickListener<LeftDrawerItem>
)
    : BaseExpandableListAdapter()
{

    override fun getChild(groupPosition: Int, childPosition: Int): SecondSubcategory = categories[groupPosition].subcategories[childPosition]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup): View
    {
        var convertView = convertView
        val subCategory = getChild(groupPosition, childPosition)
        convertView = LayoutInflater.from(parent.context).
                inflate(R.layout.item_expandable_list_child, parent, false)

        val textView_name = convertView.findViewById<View>(R.id.textView_name) as TextView
        textView_name.text = subCategory.name


        convertView.setOnClickListener{
            itemClickListener.onClick(
                convertView, groupPosition, LeftDrawerItem(
                    subCategory.categoryId, subCategory.name, isCategory = false
                )
            )
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int = categories[groupPosition].subcategories.size


    override fun getGroup(groupPosition: Int): Subcategory = categories[groupPosition]


    override fun getGroupCount(): Int = categories.size


    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View
    {
        var convertView = convertView

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.item_expandable_list_group, null)
        val logo = convertView.findViewById<View>(R.id.logo) as AppCompatImageView
        val expandableIcon = convertView.findViewById<View>(R.id.expandableIcon) as AppCompatImageView
        val text = convertView.findViewById<View>(R.id.textView_name) as TextView
        val subCategory = getGroup(groupPosition)
        text.text = subCategory.name
        if (getChildrenCount(groupPosition) < 1) {
            expandableIcon.visibility = View.INVISIBLE
            convertView?.setOnClickListener{
                itemClickListener.onClick(
                    text, groupPosition, LeftDrawerItem(
                        subCategory.categoryId, subCategory.name, isCategory = false
                    )
                )
            }
        }
        else
        {

            val typeface = ResourcesCompat.getFont(text.context, R.font.montserrat_regular)
            text.setTypeface(typeface, Typeface.BOLD)

            logo.visibility = View.GONE

            expandableIcon.visibility = View.VISIBLE
            if (isExpanded)
                expandableIcon.setImageResource(R.drawable.ic_list_collapse)
            else
                expandableIcon.setImageResource(R.drawable.ic_list_expand)
        }

        text.setOnClickListener{
            itemClickListener.onClick(
                text, groupPosition, LeftDrawerItem(
                    subCategory.categoryId, subCategory.name, isCategory = false
                )
            )
        }

        logo.loadImg(subCategory.iconUrl)

        return convertView!!
    }

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}