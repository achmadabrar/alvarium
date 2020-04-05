package com.bs.ecommerce.home.category

import android.app.FragmentManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView

import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.data.Category

/**
 * Created by Arif Islam on 23-Feb-17.
 */

class CategoryListAdapter(private val context: Context, private val categoryList: List<Category>, private val fragment: androidx.fragment.app.Fragment) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()
{
    private val productClickListener: OnItemClickListener? = null

    interface OnItemClickListener
    {
        fun onItemClick(view: View, category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ProductSummaryHolder(itemView)

    }

    override fun onBindViewHolder(bindViewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)
    {

        val categoryModel = categoryList[position]
        val holder = bindViewHolder as ProductSummaryHolder
        holder.topCategoryName.text = categoryModel.name.toUpperCase()

        holder.categoryChildList.setAdapter(SubCategoryListAdapter(context, categoryModel.subcategories, fragment))

        holder.topCategoryName.setOnClickListener(CategoryOnClicklistener(categoryModel))
        holder.categoryChildList.setGroupIndicator(null)
    }

    override fun getItemViewType(position: Int): Int {
        return ViewType.LIST
    }


    override fun getItemCount(): Int {
        return categoryList.size ?: 0
    }


    private inner class ProductSummaryHolder internal constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var topCategoryName: TextView
        internal var categoryChildList: ExpandableListView

        init {
            topCategoryName = itemView.findViewById<View>(R.id.topCategoryName) as TextView
            categoryChildList = itemView.findViewById<View>(R.id.categoryChildList) as ExpandableListView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            productClickListener?.onItemClick(v, categoryList[adapterPosition])
        }

    }

    private inner class CategoryOnClicklistener(internal var category: Category) : View.OnClickListener {
        override fun onClick(v: View) {

/*            ProductService.productId = category.id.toLong()

            gotocategoryListPage(category)*/
        }
    }

    private fun gotocategoryListPage(category: Category)
    {
/*        Utility.closeLeftDrawer()

        fragment.fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        fragment.fragmentManager!!.beginTransaction()
                .replace(R.id.container, categoryListFragmentFor3_8.newInstance(category.name, category.id))
                .addToBackStack(null)
                .commit()*/
    }


}
