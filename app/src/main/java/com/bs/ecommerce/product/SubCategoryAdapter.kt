package com.bs.ecommerce.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.SubCategory


class SubCategoryAdapter(
    private var categories: List<SubCategory>
) : BaseAdapter() {


    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): SubCategory = categories[position]


    override fun getItemId(position: Int): Long = categories[position].id?.toLong() ?: 0


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = parent.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val row = inflater.inflate(R.layout.item_subcat_selection, parent, false)
        val title = row.findViewById<View>(R.id.topCategoryName) as TextView
        title.text = this.categories[position].name
        return row
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {

        val inflater = parent.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val row = inflater.inflate(R.layout.item_subcat_selection, parent, false)
        val title = row.findViewById<View>(R.id.topCategoryName) as TextView
        title.text = this.categories[position].name
        return row
    }
}

