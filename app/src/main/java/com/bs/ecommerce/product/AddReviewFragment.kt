package com.bs.ecommerce.product

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.viewmodel.ReviewViewModel
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_add_review.*
import kotlin.math.roundToInt

class AddReviewFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_add_review

    override fun getRootLayout(): RelativeLayout? = addReviewRootLayout

    override fun createViewModel(): BaseViewModel = ReviewViewModel()

    override fun getFragmentTitle(): Int = R.string.title_Add_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            setupView()
        }
    }

    private fun setupView() {
        btnSubmit.setOnClickListener { submitIfFormIsValid() }

        // set minimum rating to 1 star
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating < 1.0f)
                ratingBar.rating = 1.0f
        }
    }

    private fun submitIfFormIsValid() {
        val title = etReviewTitle.text.toString().trim()
        val text = etReviewText.text.toString().trim()
        //val rating: Int = ratingBar.rating.roundToInt()

        if(title.isEmpty()) {
            toast(getString(R.string.validation_toast, etReviewTitle.hint.toString()))
            return
        }

        if(text.isEmpty()) {
            toast(getString(R.string.validation_toast, etReviewText.hint.toString()))
            return
        }

        /*if(rating == 0) {
            toast(getString(R.string.validation_toast, etEnquiry.hint.toString()))
            return
        }*/

        // TODO api call here
    }


}