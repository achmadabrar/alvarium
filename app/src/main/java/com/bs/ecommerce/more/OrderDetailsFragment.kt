package com.bs.ecommerce.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.cart.GiftCardAdapter
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.adapter.OrderNotesAdapter
import com.bs.ecommerce.more.model.OrderModel
import com.bs.ecommerce.more.model.OrderModelImpl
import com.bs.ecommerce.more.viewmodel.OrderViewModel
import com.bs.ecommerce.product.model.data.Item
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.product.model.data.OrderNotes
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_customer_order_detail.*
import kotlinx.android.synthetic.main.item_order_details.view.*
import kotlinx.android.synthetic.main.table_order_total.*
import java.lang.ref.WeakReference

class OrderDetailsFragment : BaseFragment() {
    private lateinit var model: OrderModel

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_ORDER_DETAILS)

    override fun getLayoutId(): Int = R.layout.fragment_customer_order_detail

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel = OrderViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            val orderId = arguments?.getInt(ORDER_ID)

            if (orderId == null) {
                toast(DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))

                //requireActivity().supportFragmentManager.popBackStackImmediate()
                return
            }

            model = OrderModelImpl()
            viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

            (viewModel as OrderViewModel).getOrderDetails(orderId, model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as OrderViewModel).orderDetailsLD.observe(viewLifecycleOwner, Observer { data->
            initView(data)
        })

        (viewModel as OrderViewModel).isLoadingLD.observe(
            viewLifecycleOwner,
            Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

        (viewModel as OrderViewModel).reorderLD.observe(
            viewLifecycleOwner,
            Observer { reorder ->

                if (reorder?.getContentIfNotHandled() == true
                    && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    (requireActivity() as BaseActivity).goMenuItemFragment(CartFragment())
                }
            })
    }

    private fun initView(data: OrderDetailsData) {
        val orderDetails = DbHelper.getString(Const.ORDER_DATE)
            .plus(": ")
            .plus(
                TextUtils().tzTimeConverter(
                    data.createdOn,
                    WeakReference(requireContext())
                )
            ).plus("\n").plus(DbHelper.getString(Const.ORDER_STATUS)).plus(": ")
            .plus(data.orderStatus).plus("\n")
            .plus(DbHelper.getString(Const.ORDER_TOTAL)).plus(": ").plus(data.orderTotal)

        orderDetailsCard.tvCardTitle.text = DbHelper.getString(Const.TITLE_ORDER_DETAILS)
        orderDetailsCard.tvCardDetails.text = DbHelper.getString(Const.ORDER_NUMBER).plus(" ").plus(data.customOrderNumber)
        orderDetailsCard.tvCardDetails2.text = orderDetails

        shippingAddressCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_ADDRESS_TAB)
        shippingAddressCard.tvCardDetails.text = data.shippingAddress?.firstName.plus(" ").plus(data.shippingAddress?.lastName)
        shippingAddressCard.tvCardDetails2.text = TextUtils().getFormattedAddress(
            data.shippingAddress, WeakReference(requireContext())
        )

        billingAddressCard.tvCardTitle.text = DbHelper.getString(Const.BILLING_ADDRESS_TAB)
        billingAddressCard.tvCardDetails.text = data.billingAddress?.firstName.plus(" ").plus(data.billingAddress?.lastName)
        billingAddressCard.tvCardDetails2.text = TextUtils().getFormattedAddress(
            data.billingAddress, WeakReference(requireContext())
        )

        shippingMethodCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_METHOD)
        shippingMethodCard.tvCardDetails.text = data.shippingMethod
        shippingMethodCard.tvCardDetails2.text = data.shippingStatus
        shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

        paymentMethodCard.tvCardTitle.text = DbHelper.getString(Const.PAYMENT_METHOD)
        paymentMethodCard.tvCardDetails.text = data.paymentMethod
        paymentMethodCard.tvCardDetails2.text = data.paymentMethodStatus
        paymentMethodCard.ivCardThumb.visibility = View.VISIBLE


        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = OrderItemsAdapter(data.items ?: listOf())
        }

        populateOrderTotal(data)

        // populate order notes
        if(!data.orderNotes.isNullOrEmpty()) {
            orderNotesLayout?.visibility = View.VISIBLE
            tvOrderNotes.text = DbHelper.getString(DbHelper.getString(Const.ORDER_NOTES))

            rvOrderNotes?.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                addItemDecoration(RecyclerViewMargin(15, 1, true))

                adapter = OrderNotesAdapter(data.orderNotes, object : ItemClickListener<OrderNotes> {
                    override fun onClick(view: View, position: Int, data: OrderNotes) {
                        toast("Notes")
                    }
                })
            }
        }

        fabPdfInvoice?.setOnClickListener {
            if(hasDiskWritePermission()) {
                data.id?.let { (viewModel as OrderViewModel).downloadPdfInvoice(it, model) }
            }
        }

        btnReorder?.setOnClickListener {
            (viewModel as OrderViewModel).reorder(data.id ?: -1, model)
        }

        orderDetailsScrollView.visibility = View.VISIBLE

        fabPdfInvoice?.visibility = if (data.pdfInvoiceDisabled == true) View.GONE
        else View.VISIBLE

        btnReorder?.visibility = if (data.isReOrderAllowed == true) View.VISIBLE
        else View.GONE
    }

    private fun populateOrderTotal(data: OrderDetailsData) {
        btnReorder?.text = DbHelper.getString(Const.ORDER_REORDER)

        subTotalKey?.text = DbHelper.getString(Const.SUB_TOTAL)
        shippingKey?.text = DbHelper.getString(Const.SHIPPING)
        taxKey?.text = DbHelper.getString(Const.TAX)
        discountKey?.text = DbHelper.getString(Const.DISCOUNT)
        totalKey?.text = DbHelper.getString(Const.TOTAL)

        tvProductsTitleOfOrderPage?.text = DbHelper.getString(Const.PRODUCTS)
        tvOrderCalculationOfOrderPage?.text = DbHelper.getString(Const.ORDER_CALCULATION)


        with(data) {
            
            tvSubTotal?.text = orderSubtotal
            tvShippingCharge?.text = orderShipping


            if (displayTax==true && tax != null) {

                if (displayTaxRates == true)
                    taxRates?.get(0)?.rate?.let { taxKey?.text = "${DbHelper.getString(Const.TAX)} $it%" }

                tvTax?.text = tax
            } else
                taxLayout?.visibility = View.GONE

            tvTotal?.text = orderTotal

            if (orderTotalDiscount != null) {
                discountLayout?.visibility = View.VISIBLE
                tvDiscount?.text = orderTotalDiscount
                underDiscountDivider?.visibility = View.VISIBLE
            } else
                discountLayout?.visibility = View.GONE

            if (giftCards != null && giftCards.isNotEmpty()) {
                giftCardLayout?.visibility = View.VISIBLE

                underGiftCardDivider?.visibility = View.VISIBLE

                val giftCardAdapter = GiftCardAdapter(requireContext(), giftCards)
                giftCardRecyclerList?.layoutManager =
                    LinearLayoutManager(activity)
                giftCardRecyclerList?.adapter = giftCardAdapter
            } else
                giftCardLayout?.visibility = View.GONE

            if (orderTotal.isNullOrEmpty()) {
                tvTotal?.text = DbHelper.getString(Const.CALCULATED_DURING_CHECKOUT)
                tvTotal?.setTextColor(android.graphics.Color.RED)
            }

            if (orderShipping.isNullOrEmpty()) {
                tvTotal?.text = DbHelper.getString(Const.CALCULATED_DURING_CHECKOUT)
                tvTotal?.setTextColor(android.graphics.Color.RED)
            }
        }
    }

    //--------------------

    inner class OrderItemsAdapter(
        private val list: List<Item>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_order_details, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val item = list[position]

            holder.itemView.tvProductName.text = item.productName

            holder.itemView.tv1.text =
                DbHelper.getString(Const.ORDER_PRICE).plus(": ").plus(item.unitPrice).plus("  ")
                DbHelper.getString(Const.ORDER_QUANTITY).plus(": ").plus(item.quantity).plus("\n")
                DbHelper.getString(Const.ORDER_TOTAL_).plus(": ").plus(item.subTotal)
        }
    }

    companion object {
        @JvmStatic
        private val ORDER_ID = "orderId"

        @JvmStatic
        fun newInstance(orderId: Int): OrderDetailsFragment {
            val fragment = OrderDetailsFragment()
            val args = Bundle()
            args.putInt(ORDER_ID, orderId)
            fragment.arguments = args
            return fragment
        }
    }
}