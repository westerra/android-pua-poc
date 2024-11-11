package com.westerra.release.bottommenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.westerra.release.WesterraApplication

class InterceptAdvertisementDetector
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    override fun onAttachedToWindow() {
        WesterraApplication.getInstance().getActivity() ?. let {
            // InterceptAdvertisementAlert(activity = it).showCustom()
        }
        super.onAttachedToWindow()
    }
}
