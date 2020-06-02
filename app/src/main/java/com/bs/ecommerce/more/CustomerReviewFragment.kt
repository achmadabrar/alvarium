package com.bs.ecommerce.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.ReviewModel
import com.bs.ecommerce.more.model.ReviewModelImpl
import com.bs.ecommerce.more.viewmodel.ReviewViewModel
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.model.data.ProductReview
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_customer_reviews.*
import kotlinx.android.synthetic.main.item_customer_review.view.*

class CustomerReviewFragment : BaseFragment() {

    private lateinit var model: ReviewModel

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_REVIEW)

    override fun getLayoutId(): Int = R.layout.fragment_customer_reviews

    override fun getRootLayout(): RelativeLayout? = customerReviewRootLayout

    override fun createViewModel(): BaseViewModel = ReviewViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            model = ReviewModelImpl()
            viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

            (viewModel as ReviewViewModel).getMyReviews(model)

            setupView()
        } else {
            (viewModel as ReviewViewModel).shouldAppend = false
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as ReviewViewModel).apply {

            myReviewsLD.observe(viewLifecycleOwner, Observer { list ->

                if (list.isNullOrEmpty() && !lastPageReached) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE

                    if (list?.isNotEmpty() == true)
                        (rvReview.adapter as ReviewAdapter).addData(list, shouldAppend)
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

    private fun setupView() {
        tvNoData.text = DbHelper.getString(Const.COMMON_NO_DATA)

        val mLayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        rvReview?.apply {
            setHasFixedSize(true)

            layoutManager = mLayoutManager

            addItemDecoration(RecyclerViewMargin(10, 1, true))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    val totalItemCount = mLayoutManager.itemCount
                    val lastVisible = mLayoutManager.findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisible == totalItemCount - 1

                    if (totalItemCount > 0 && endHasBeenReached) {

                        "nop_".showLog("last item of Product list is visible")

                        (viewModel as ReviewViewModel).getMyReviews(model)
                    }
                }
            })

            adapter = ReviewAdapter()
        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: MutableList<ProductReview> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_customer_review, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = list[position]

            holder.itemView.apply {

                tvReviewTitle.text = item.title
                tvReviewText.text = item.reviewText
                tvDate.text = item.writtenOnStr

                ratingBar.rating = (item.rating ?: 0).toFloat()

                tvProductName.text = item.productName
                tvProductName.visibility = View.VISIBLE

                setOnClickListener {
                    if (item.productId != null)
                        replaceFragmentSafely(ProductDetailFragment.newInstance(
                            item.productId.toLong(), item.productName)
                        )
                }
            }
        }

        fun addData(newData: List<ProductReview>, append: Boolean) {


            if(append) {
                val oldSize = list.size
                list.addAll(newData)

                notifyItemRangeInserted(oldSize, newData.size)
            } else {
                list.clear()
                list.addAll(newData)
                notifyDataSetChanged()
            }

        }

    }
}