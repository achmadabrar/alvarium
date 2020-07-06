package com.bs.ecommerce.account.orders.shipments

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
import com.bs.ecommerce.account.orders.OrderDetailsFragment
import com.bs.ecommerce.account.orders.OrderViewModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.orders.model.OrderModel
import com.bs.ecommerce.account.orders.model.OrderModelImpl
import com.bs.ecommerce.account.orders.model.data.ShipmentDetailsData
import com.bs.ecommerce.account.orders.model.data.ShippedItem
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_customer_order_detail.orderDetailsScrollView
import kotlinx.android.synthetic.main.fragment_customer_order_shipment_detail.*
import kotlinx.android.synthetic.main.item_order_details.view.*
import java.lang.ref.WeakReference

class ShipmentDetailsFragment : BaseFragment() {
    private lateinit var model: OrderModel

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_ORDER_SHIPMENT_DETAILS)

    override fun getLayoutId(): Int = R.layout.fragment_customer_order_shipment_detail

    override fun getRootLayout(): RelativeLayout? = customerOrderShipmentRootLayout

    override fun createViewModel(): BaseViewModel = OrderViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            val shipmentId = arguments?.getInt(ORDER_ID)

            if (shipmentId == null) {
                toast(DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))

                //requireActivity().supportFragmentManager.popBackStackImmediate()
                return
            }

            model = OrderModelImpl()
            viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

            (viewModel as OrderViewModel).getShipmentDetails(shipmentId, model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {


        (viewModel as OrderViewModel).apply {

            shipmentDetailsLD.observe(viewLifecycleOwner, Observer { data->
                initView(data)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                showHideLoader(isShowLoader) })
        }
    }


    private fun initView(data: ShipmentDetailsData)
    {

        shipmentInfoCard.tvCardTitle.text = DbHelper.getString(Const.ORDER_SHIPMENT_ID).plus(" ") + data.id
        shipmentInfoCard.tvCardDetails.text = DbHelper.getString(Const.ORDER_NUMBER).plus(" ").plus(data.order?.customOrderNumber)
        shipmentInfoCard.tvCardDetails2.text =

            DbHelper.getString(Const.SHIPPING_METHOD).plus(" : ").plus(data.order?.shippingMethod)
            .plus("\n")
            .plus(DbHelper.getString(Const.ORDER_DATE_SHIPPED).plus(" : ").plus( TextUtils().tzTimeConverter(data.shippedDate, WeakReference(requireActivity()))
            .plus("\n"))
            .plus(DbHelper.getString(Const.ORDER_DATE_DELIVERED).plus(" : ").plus( TextUtils().tzTimeConverter(data.deliveryDate, WeakReference(requireActivity()))
            .plus("\n")
            .plus(DbHelper.getString(Const.SHIPMENT_TRACKING_NUMBER)).plus(" : ").plus(data.trackingNumber)
            )))

        data.order?.shippingAddress?.let {

            shippingAddressCard.tvCardTitle.text = DbHelper.getString(Const.SHIPPING_ADDRESS_TAB)
            shippingAddressCard.tvCardDetails.text = it.firstName.plus(" ").plus(it.lastName)
            shippingAddressCard.tvCardDetails2.text = TextUtils().getFormattedAddress(
              it, WeakReference(requireContext())
            )

        }



        shippedProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = ShippedItemsAdapter(data.items ?: listOf())
        }

        shipmentDetailsScrollView?.visibility = View.VISIBLE

    }
    inner class ShippedItemsAdapter(
        private val list: List<ShippedItem>
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
                DbHelper.getString(Const.SKU).plus(": ").plus(item.sku).plus("\n")
                .plus(DbHelper.getString(Const.ORDER_QUANTITY_SHIPPED)).plus(": ").plus(item.quantityShipped)

            /*holder.itemView.tv1.text =
                DbHelper.getString(Const.ORDER_PRICE).plus(": ").plus(item.unitPrice).plus("  ")
            DbHelper.getString(Const.ORDER_QUANTITY).plus(": ").plus(item.quantity).plus("\n")
            DbHelper.getString(Const.ORDER_TOTAL_).plus(": ").plus(item.subTotal)*/
        }
    }



    companion object {
        @JvmStatic
        private val ORDER_ID = "shipmentId"

        @JvmStatic
        fun newInstance(shipmentId: Int): ShipmentDetailsFragment {
            val fragment = ShipmentDetailsFragment()
            val args = Bundle()
            args.putInt(ORDER_ID, shipmentId)
            fragment.arguments = args
            return fragment
        }
    }
}