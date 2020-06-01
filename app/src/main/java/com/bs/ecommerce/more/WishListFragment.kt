package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.more.adapter.WishListAdapter
import com.bs.ecommerce.more.model.WishListModel
import com.bs.ecommerce.more.model.WishListModelImpl
import com.bs.ecommerce.more.viewmodel.WishListViewModel
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.model.data.WishListItem
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_wishlist.*

class WishListFragment : BaseFragment() {

    private lateinit var model: WishListModel
    private lateinit var listAdapter: WishListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_wishlist

    override fun getRootLayout(): RelativeLayout? = wishListRootLayout

    override fun createViewModel(): BaseViewModel = WishListViewModel()

    override fun getFragmentTitle(): Int = R.string.title_wishlist // DbHelper.getString(Const.ACCOUNT_WISHLIST)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            "nop_".showLog("creating view")

            model = WishListModelImpl()
            viewModel = ViewModelProvider(this).get(WishListViewModel::class.java)

            (viewModel as WishListViewModel).getWishList(model)

            initView()
        }
        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {

        (viewModel as WishListViewModel).apply {

            wishListLD.observe(viewLifecycleOwner, Observer { data ->

                btnAddAllToCart.visibility = if (data?.items?.isNotEmpty() == true)
                    View.VISIBLE else View.GONE

                tvNoData.visibility = if (data?.items.isNullOrEmpty())
                    View.VISIBLE else View.GONE

                listAdapter.addData(data?.items)
            })

            goToCartLD.observe(viewLifecycleOwner, Observer { goToCart ->

                if (goToCart && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    eventHandled() // reset gotoCartLD value
                    replaceFragmentSafely(CartFragment())
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    private fun initView() {

        btnAddAllToCart.setOnClickListener {
            (viewModel as WishListViewModel).moveAllItemsToCart(model)
        }

        val clickListener = object : ItemClickListener<WishListItem> {

            override fun onClick(view: View, position: Int, data: WishListItem) {

                when (view.id) {

                    R.id.icRemoveItem ->
                        (viewModel as WishListViewModel)
                            .removeItemFromWishList(data.id, model)

                    R.id.btnAddToCart ->
                        (viewModel as WishListViewModel)
                            .moveItemToCart(data.id, model)

                    R.id.itemView ->
                        data.productId?.let {
                            replaceFragmentSafely(ProductDetailFragment.newInstance(it, data.productName))
                        }
                }
            }
        }

        rvWishList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))

            listAdapter = WishListAdapter(clickListener)
            adapter = listAdapter
        }

    }

}