package com.bs.ecommerce.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.viewmodel.WishListViewModel
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.fragment_wishlist.*
import kotlinx.android.synthetic.main.item_wish_list.view.*

class WishListFragment: BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_wishlist

    override fun getRootLayout(): RelativeLayout? = wishListRootLayout

    override fun createViewModel(): BaseViewModel = WishListViewModel()

    override fun getFragmentTitle(): Int = R.string.title_wishlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        btnAddAllToCart.visibility = View.VISIBLE

        btnAddAllToCart.setOnClickListener {
            // TODO api call
        }

        populateOrderList()
    }

    private fun populateOrderList() {
        val items: MutableList<TempItem> = mutableListOf()

        for (i in 1..4) {
            items.add(TempItem("Item $i", "$${i * 167}"))
        }

        rvWishList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))
            adapter = TempAdapter(items)
        }
    }

    //-----------------------

    inner class TempAdapter(
        private val list: List<TempItem>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_wish_list, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tvProductName.text = list[position].name
            holder.itemView.tvProductPrice.text = list[position].price

            holder.itemView.ivProductThumb.loadImg("")

            holder.itemView.btnAddToCart.setOnClickListener {
                // TODO api call
            }

            holder.itemView.icRemoveItem.setOnClickListener {
                // TODO api call
            }
        }

    }

    inner class TempItem(
        val name: String,
        val price: String
    )

}