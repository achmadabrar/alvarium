package com.bs.ecommerce.customViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * Created by Ashraful on 11/26/2015.
 */
class RadioGridGroupforReyMaterial : androidx.gridlayout.widget.GridLayout {

    var checkedAppCompatRadioButtonId = -1
        private set
    private var mChildOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    private var mProtectFromCheckedChange = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null
    private var mPassThroughListener: PassThroughHierarchyChangeListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        mChildOnCheckedChangeListener = CheckedStateTracker()
        mPassThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(mPassThroughListener)
    }

    override fun setOnHierarchyChangeListener(listener: ViewGroup.OnHierarchyChangeListener) {
        mPassThroughListener!!.mOnHierarchyChangeListener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (checkedAppCompatRadioButtonId != -1) {
            mProtectFromCheckedChange = true
            setCheckedStateForView(checkedAppCompatRadioButtonId, true)
            mProtectFromCheckedChange = false
            setCheckedId(checkedAppCompatRadioButtonId)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is AppCompatRadioButton) {
            if (child.isChecked) {
                mProtectFromCheckedChange = true
                if (checkedAppCompatRadioButtonId != -1) {
                    setCheckedStateForView(checkedAppCompatRadioButtonId, false)
                }
                mProtectFromCheckedChange = false
                setCheckedId(child.id)
            }
        }
        /*if (child instanceof CheckableLinearLayout) {
            final CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) child;
            if (checkableLinearLayout.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(checkableLinearLayout.getId());
            }
        }*/

        super.addView(child, index, params)
    }

    fun check(id: Int) {
        if (id != -1 && id == checkedAppCompatRadioButtonId) {
            return
        }

        if (checkedAppCompatRadioButtonId != -1) {
            setCheckedStateForView(checkedAppCompatRadioButtonId, false)
        }

        if (id != -1) {
            setCheckedStateForView(id, true)
        }

        setCheckedId(id)
    }

    private fun setCheckedId(id: Int) {
        checkedAppCompatRadioButtonId = id
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener!!.onCheckedChanged(this, checkedAppCompatRadioButtonId)
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById<View>(viewId)
        if (checkedView != null && checkedView is AppCompatRadioButton) {
            checkedView.isChecked = checked
        }
    }

    fun clearCheck() {
        check(-1)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        mOnCheckedChangeListener = listener
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = RadioGridGroupforReyMaterial::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = RadioGridGroupforReyMaterial::class.java.name
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: RadioGridGroupforReyMaterial, checkedId: Int)
    }

    private inner class CheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            if (mProtectFromCheckedChange) {
                return
            }

            mProtectFromCheckedChange = true
            if (checkedAppCompatRadioButtonId != -1) {
                setCheckedStateForView(checkedAppCompatRadioButtonId, false)
            }
            mProtectFromCheckedChange = false

            val id = buttonView.id
            setCheckedId(id)
        }
    }

    inner class PassThroughHierarchyChangeListener : ViewGroup.OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: ViewGroup.OnHierarchyChangeListener? = null

        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@RadioGridGroupforReyMaterial && child is AppCompatRadioButton) {
                val id = child.getId()
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    Log.d("need Id", "")
                    //     id = ViewUtils.generateViewId();
                    child.setId(id)
                }
                child.setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener)
            }

            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@RadioGridGroupforReyMaterial && child is AppCompatRadioButton) {
                child.setOnCheckedChangeListener(null)
            }

            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }
}
