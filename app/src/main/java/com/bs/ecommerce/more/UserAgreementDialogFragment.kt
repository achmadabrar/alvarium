package com.bs.ecommerce.more

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.bs.ecommerce.R
import com.bs.ecommerce.more.downloadableProducts.DownloadableProductListFragment
import com.bs.ecommerce.product.model.data.UserAgreementData
import kotlinx.android.synthetic.main.user_agreement_dialog.*

class UserAgreementDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.user_agreement_dialog, container, false
    )

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            val params: ViewGroup.LayoutParams = attributes
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT
            attributes = params as WindowManager.LayoutParams
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val data = arguments?.getParcelable<UserAgreementData>(KEY_)

        data?.let { userAgreementData ->
            tvAgreementText?.text = userAgreementData.userAgreementText
            btnProceed?.setOnClickListener {

                if(cbAgree?.isChecked == true) {
                    val fragment = requireActivity().supportFragmentManager.findFragmentByTag(
                        DownloadableProductListFragment::class.java.simpleName
                    )

                    if(fragment is DownloadableProductListFragment)
                        fragment.onUserAgreeClicked(userAgreementData)
                }

                dismiss()
            }
        }

    }

    companion object {
        @JvmStatic
        private val KEY_: String = "KEY_"

        fun newInstance(url: UserAgreementData): UserAgreementDialogFragment {
            val fragment = UserAgreementDialogFragment()

            fragment.arguments = Bundle().apply {
                putParcelable(KEY_, url)
            }

            return fragment
        }
    }
}