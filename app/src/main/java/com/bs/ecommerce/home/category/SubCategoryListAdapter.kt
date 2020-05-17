package com.bs.ecommerce.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.model.data.SecondSubcategory
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.product.ProductListFragment
import com.bs.ecommerce.utils.loadImg

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
        val logo = convertView.findViewById<View>(R.id.logo) as AppCompatImageView
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
            if (isExpanded)
                expandableIcon.setImageResource(R.drawable.ic_list_collapse)
            else
                expandableIcon.setImageResource(R.drawable.ic_list_expand)
        }

        text.setOnClickListener( CategoryonClicklistener(subCategory.categoryId, subCategory.name))

        logo.loadImg(subCategory.iconUrl)

        return convertView!!
    }

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true


    private inner class CategoryonClicklistener(private val id: Int, private val name: String) : View.OnClickListener
    {

        override fun onClick(v: View)
        {
            val activity = fragment.requireActivity()

            if(activity is MainActivity) {
                activity.closeDrawer()

                activity.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

                activity.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.layoutFrame,
                        ProductListFragment.newInstance(name, id,
                            ProductListFragment.GetBy.CATEGORY)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}