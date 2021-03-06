package com.bs.ecommerce.home.category


/*
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
        return CategoryViewHolder(itemView)

    }

    override fun onBindViewHolder(bindViewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)
    {

        val categoryModel = categoryList[position]
        val holder = bindViewHolder as CategoryViewHolder
        holder.topCategoryName.text = categoryModel.name.toUpperCase()

        holder.categoryChildList.setAdapter(SubCategoryListAdapter(context, categoryModel.subcategories, fragment))

        holder.topCategoryName.setOnClickListener(CategoryOnClickListener(categoryModel))


        if(categoryModel.subcategories.isNotEmpty())
            holder.topCategoryExpandableButton.visibility = View.VISIBLE
        else
            holder.topCategoryExpandableButton.visibility = View.GONE

        holder.topCategoryExpandableButton.setOnClickListener {

            if(categoryList[position].childVisible)
            {
                holder.topCategoryExpandableButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_left_category))
                categoryList[position].childVisible = false
                holder.categoryChildList.visibility = View.GONE

            }
            else
            {
                holder.topCategoryExpandableButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_minus_left_category))
                categoryList[position].childVisible = true
                holder.categoryChildList.visibility = View.VISIBLE
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return ViewType.LIST
    }


    override fun getItemCount(): Int {
        return categoryList.size ?: 0
    }


    private inner class CategoryViewHolder internal constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var topCategoryName: TextView
        internal var topCategoryExpandableButton : ImageView
        internal var categoryChildList: ExpandableListView

        init {
            topCategoryName = itemView.findViewById<View>(R.id.topCategoryName) as TextView
            topCategoryExpandableButton = itemView.findViewById<View>(R.id.topCategoryExpandableButton) as ImageView
            categoryChildList = itemView.findViewById<View>(R.id.categoryChildList) as ExpandableListView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            productClickListener?.onItemClick(v, categoryList[adapterPosition])
        }

    }

    private inner class CategoryOnClickListener(internal var category: Category) : View.OnClickListener {
        override fun onClick(v: View) {
            goToCategoryListPage(category)
        }
    }

    private fun goToCategoryListPage(category: Category)
    {
        val activity = fragment.requireActivity()

        if(activity is MainActivity) {
            activity.closeDrawer()

            activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            activity.supportFragmentManager.beginTransaction()
                .replace(
                    R.id.layoutFrame,
                    ProductListFragment.newInstance(category.name, category.categoryId,
                        ProductListFragment.GetBy.CATEGORY)
                )
                .addToBackStack(null)
                .commit()
        }
    }


}
*/
