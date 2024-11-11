package com.westerra.release.btp.components

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.westerra.release.R

class CustomSwipeRefreshLayout(
    context: Context,
    attributeSet: AttributeSet,
) : SwipeRefreshLayout(context, attributeSet) {
    init {
        setColorSchemeResources(R.color.primaryColor)
    }
}
