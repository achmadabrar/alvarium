package com.bs.ecommerce.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout

/**
 * Created by Ashraful on 1/27/2016.
 * ****************************************************************************
 * Copyright 2013 Chris Banes.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */

/*******************************************************************************
 * Copyright 2013 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class CheckableLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), Checkable {

    private var mChecked = false

    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    /**
     * Interface definition for a callback to be invoked when the checked state of this View is
     * changed.
     */
    interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkableView The view whose state has changed.
         * @param isChecked     The new checked state of checkableView.
         */
        fun onCheckedChanged(checkableView: View, isChecked: Boolean)
    }

    override fun isChecked(): Boolean = mChecked

    override fun setChecked(b: Boolean)
    {
        if (b != mChecked)
        {
            mChecked = b
            refreshDrawableState()

            if (mOnCheckedChangeListener != null)
            {
                mOnCheckedChangeListener!!.onCheckedChanged(this, mChecked)
            }
        }
    }

    override fun toggle()
    {
        isChecked = !mChecked
    }

    public override fun onCreateDrawableState(extraSpace: Int): IntArray
    {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)

        if (isChecked)
        {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    /**
     * Register a callback to be invoked when the checked state of this view changes.
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener)
    {
        mOnCheckedChangeListener = listener
    }

    companion object
    {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }

}