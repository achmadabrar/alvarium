package com.bs.ecommerce.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.review.ProductReviewFragment
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.EditTextUtils
import kotlinx.android.synthetic.main.fragment_add_review.*
import kotlin.math.roundToInt

class AddReviewFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_add_review, container, false
    )

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = LinearLayout.LayoutParams.MATCH_PARENT
        params?.height = LinearLayout.LayoutParams.WRAP_CONTENT

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        etReviewTitle.hint = DbHelper.getString(Const.REVIEW_TITLE)
        etReviewText.hint = DbHelper.getString(Const.REVIEW_TEXT)
        tvRating.text = DbHelper.getString(Const.REVIEW_RATING)
        btnSubmit.text = DbHelper.getString(Const.REVIEW_SUBMIT_BTN)

        btnSubmit.setOnClickListener { submitIfFormIsValid() }

        // set minimum rating to 1 star
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating < 1.0f)
                ratingBar.rating = 1.0f
        }
    }

    private fun submitIfFormIsValid() {
        val etUtil = EditTextUtils()

        val title = etUtil.showToastIfEmpty(etReviewTitle,
            DbHelper.getString(Const.REVIEW_TITLE_REQ)) ?: return
        val review = etUtil.showToastIfEmpty(etReviewText,
            DbHelper.getString(Const.REVIEW_TEXT_REQ)) ?: return

        val rating: Int = ratingBar.rating.roundToInt()

        if (parentFragment != null && parentFragment is ProductReviewFragment) {

            (parentFragment as ProductReviewFragment).postProductReview(
                title, review, rating
            )

            dismiss()
        }
    }


}