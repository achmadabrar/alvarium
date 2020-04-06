package com.bs.ecommerce.cart

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.LoginFragment
import com.bs.ecommerce.auth.RegisterFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.guest_checkout_dialog_fragment.*

class GuestCheckoutFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.guest_checkout_dialog_fragment, container, false
    )

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        /*if (isGuestCheckout)
            newCustomerCheckoutLayout?.visibility = View.GONE
        else
            guestCheckoutLayout?.visibility = View.GONE*/


        btnGuestCheckout.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        btnLogin.setOnClickListener(this)

        btnClose.setOnClickListener { dismiss() }
    }

    override fun onClick(v: View) {
        var fragment: BaseFragment? = null

        when (v.id) {
            R.id.btnLogin -> fragment = LoginFragment()
            R.id.btnRegister -> fragment = RegisterFragment()
            // fixme R.id.btnGuestCheckout -> fragment = CheckoutStepFragment()
        }

        fragment?.let {
            replaceFragmentSafely(it, R.id.layoutFrame)
        }
        dismiss()
    }

}