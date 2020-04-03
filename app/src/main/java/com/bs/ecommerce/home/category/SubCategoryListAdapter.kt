package com.bs.ecommerce.home.category

import android.app.FragmentManager
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.data.SecondSubcategory
import com.bs.ecommerce.main.model.data.Subcategory
import com.squareup.picasso.Picasso

/**
 * Created by bs206 on 3/16/18.
 */
class SubCategoryListAdapter(private val context: Context, private val categories: List<Subcategory>, private val fragment: androidx.fragment.app.Fragment) : BaseExpandableListAdapter()
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
        convertView = LayoutInflater.from(context).
                inflate(R.layout.item_expandable_list_child, parent, false)

        val textView_name = convertView.findViewById<View>(R.id.textView_name) as TextView
        textView_name.text = subCategory.name


        convertView.setOnClickListener(CategoryonClicklistener(subCategory.categoryId, subCategory.name))
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int = categories[groupPosition].subcategories.size


    override fun getGroup(groupPosition: Int): Subcategory = categories[groupPosition]


    override fun getGroupCount(): Int = categories.size


    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View
    {
        var convertView = convertView

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.item_expandable_list_group, null)
        val iv_icon = convertView.findViewById<View>(R.id.iv_icon) as AppCompatImageView
        val expandableIcon = convertView.findViewById<View>(R.id.expandableIcon) as AppCompatImageView
        val text = convertView.findViewById<View>(R.id.textView_name) as TextView
        val subCategory = getGroup(groupPosition)
        text.text = subCategory.name
        if (getChildrenCount(groupPosition) < 1) {
            expandableIcon.visibility = View.INVISIBLE
            convertView?.setOnClickListener(CategoryonClicklistener(subCategory.categoryId, subCategory.name))
        }
        else
        {
            expandableIcon.visibility = View.VISIBLE
           /* if (isExpanded)
                expandableIcon.setImageResource(R.drawable.ic_chevron_up)
            else
                expandableIcon.setImageResource(R.drawable.ic_chevron_down)*/
        }

        text.setOnClickListener( CategoryonClicklistener(subCategory.categoryId, subCategory.name))

        Picasso.with(context).load(subCategory.iconUrl).fit().centerInside().into(iv_icon)

        return convertView!!
    }

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true


    private inner class CategoryonClicklistener(private val id: Int, private val name: String) : View.OnClickListener
    {

        override fun onClick(v: View)
        {
           /* ProductService.productId = id.toLong()
            Utility.closeLeftDrawer()
            fragment.fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragment.fragmentManager!!.beginTransaction()
                    .replace(R.id.container, ProductListFragmentFor3_8.newInstance(name, id))
                    .addToBackStack(null)
                    .commit()*/
        }
    }
}