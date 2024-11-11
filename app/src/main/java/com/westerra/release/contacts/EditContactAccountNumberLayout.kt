package com.westerra.release.contacts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.appbar.MaterialToolbar
import com.westerra.release.R

class EditContactAccountNumberLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        getParentToolbarTitle() ?. let {
            visibility = if (it == "Edit Contact") {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    private fun getParentToolbarTitle(): String? {
        val groupParent = this.parent.parent.parent as? ViewGroup
        if (groupParent != null) {
            val toolbar = groupParent.findViewById<MaterialToolbar>(
                R.id.rcj_contactFormScreen_toolbar,
            )
            return toolbar?.title?.toString()
        }
        return null
    }
}
