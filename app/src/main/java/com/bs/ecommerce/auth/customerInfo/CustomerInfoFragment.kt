package com.bs.ecommerce.auth.customerInfo


import android.os.Bundle
import android.view.View
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.RegisterFragment
import com.bs.ecommerce.auth.register.RegistrationViewModel
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData


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
        // custom attribute fields value
        val formValue = customAttributeManager
            ?.getFormData(Api.customerAttributePrefix) ?: KeyValueFormData()

        customerInfo.formValues = formValue.formValues

        (viewModel as RegistrationViewModel).postRegisterVM(customerInfo, model)
    }





}
