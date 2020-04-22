package com.bs.ecommerce.auth.customerInfo


import android.os.Bundle
import android.view.View
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.RegisterFragment
import com.bs.ecommerce.auth.register.RegistrationViewModel


class CustomerInfoFragment : RegisterFragment()
{
    override fun getFragmentTitle() = R.string.title_customer_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        (viewModel as RegistrationViewModel).getCustomerInfoVM(model)


    }

    override fun performSubmit()
    {
        if(isValidInfo)
            (viewModel as RegistrationViewModel).postRegisterVM(customerInfo, model)
    }





}
