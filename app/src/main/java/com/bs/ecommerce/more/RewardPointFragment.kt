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
import com.bs.ecommerce.more.model.RewardPointModel
import com.bs.ecommerce.more.model.RewardPointModelImpl
import com.bs.ecommerce.more.viewmodel.RewardPointViewModel
import com.bs.ecommerce.product.model.data.RewardPoint
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.fragment_reward_point.*
import kotlinx.android.synthetic.main.item_reward_points.view.*
import java.lang.ref.WeakReference

class RewardPointFragment : BaseFragment() {

    private lateinit var model: RewardPointModel

    override fun getFragmentTitle() = R.string.title_reward_points

    override fun getLayoutId(): Int = R.layout.fragment_reward_point

    override fun getRootLayout(): RelativeLayout? = productReviewRootLayout

    override fun createViewModel(): BaseViewModel = RewardPointViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            model = RewardPointModelImpl()
            viewModel = ViewModelProvider(this).get(RewardPointViewModel::class.java)

            (viewModel as RewardPointViewModel).getRewardPoints(model)

            setupView()
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as RewardPointViewModel).apply {

            rewardPointLD.observe(viewLifecycleOwner, Observer { rewardPoint ->

                tvRewardBalance.text = getString(R.string.reward_summary,
                    rewardPoint.rewardPointsBalance, rewardPoint.rewardPointsAmount,
                    rewardPoint.minimumRewardPointsBalance, rewardPoint.minimumRewardPointsAmount)


                if (rewardPoint?.rewardPoints?.isNullOrEmpty() == true) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE

                    if (rewardPoint?.rewardPoints?.isNotEmpty() == true)
                        (rvReview.adapter as ReviewAdapter).addData(rewardPoint.rewardPoints)
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

        val mLayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        rvReview?.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerViewMargin(10, 1, true))
            adapter = ReviewAdapter()
        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: MutableList<RewardPoint> = mutableListOf()
        private val textUtils = TextUtils()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_reward_points, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = list[position]

            holder.itemView.apply {

                tvPoints?.text = item.points?.toString()
                tvBalance?.text = item.pointsBalance
                tvMessage?.text = item.message

                tvCreatedOn?.text = textUtils.tzTimeConverter(
                    item.createdOn, WeakReference(holder.itemView.context)
                )

                tvEndDate?.text = textUtils.tzTimeConverter(
                    item.endDate, WeakReference(holder.itemView.context)
                )
            }
        }

        fun addData(newData: List<RewardPoint>) {
            list.clear()
            list.addAll(newData)
            notifyDataSetChanged()
        }

    }

    companion object {
        @JvmStatic
        private val PRODUCT_ID = "productId"

        @JvmStatic
        fun newInstance(productId: Long): RewardPointFragment {

            return RewardPointFragment().apply {
                val args = Bundle()
                args.putLong(PRODUCT_ID, productId)
                arguments = args
            }
        }
    }
}