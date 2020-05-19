package com.bs.ecommerce.checkout

import com.bs.ecommerce.networking.NetworkConstants

/**
 * Created by bs-110 on 1/19/2016.
 */
object CheckoutConstants
{


    @JvmStatic val PaymentInfoUrl = "${NetworkConstants.SITE_URL}/nopstationcheckout/paymentinfo"
    @JvmStatic val RedirectUrl = "${NetworkConstants.SITE_URL}/nopstationcheckout/redirect"

    @JvmStatic val BILLING_ADDRESS_TAB = 0
    @JvmStatic val SHIPPING_ADDRESS_TAB = 1


    @JvmStatic val ADDRESS_TAB = 0
    @JvmStatic val SHIPPING_TAB = 1
    @JvmStatic val PAYMENT_TAB = 2
    @JvmStatic val CONFIRM_TAB = 3


    @JvmStatic val CartPage = 0
    @JvmStatic val BillingAddress = 1
    @JvmStatic val ShippingAddress = 2
    @JvmStatic val StorePickUp = 15
    @JvmStatic val ShippingMethod = 3
    @JvmStatic val PaymentMethod = 4
    @JvmStatic val PaymentInfo = 5
    @JvmStatic val ConfirmOrder = 6
    @JvmStatic val RedirectToGateway = 7
    @JvmStatic val Completed = 8
}

