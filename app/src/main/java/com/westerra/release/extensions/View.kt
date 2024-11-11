package com.westerra.release.extensions

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

fun View.fadeIn() {
    if (alpha == 1.0f && visibility == View.VISIBLE) {
        return
    }
    this.clearAnimation()
    if (alpha < 0.1f) {
        this.alpha = 0.1f
    }
    this.visibility = View.VISIBLE
    this.animate().apply {
        interpolator = LinearInterpolator()
        duration = 500
        alpha(1f)
        start()
    }
}

// View loses spacing when GONE
fun View.fadeGone() {
    fadeOut(View.GONE)
}

// View maintains spacing when INVISIBLE
fun View.fadeInvisible() {
    fadeOut(View.INVISIBLE)
}

private fun View.fadeOut(endVisibility: Int) {
    if (alpha == 0.0f || visibility == View.INVISIBLE || visibility == View.GONE) {
        alpha = 0.0f
        return
    }
    if (this.isParentAnimateLayoutChangesEnabled()) {
        // Note: If parent has animateLayoutChanges set to true, setting this view
        // visibility will cause a second animation event and the view will blink.
        Log.i("View.fadeOut", "Detected view hierarchy animation conflict.")
        visibility = endVisibility
        return
    }
    this.clearAnimation()
    if (alpha < 0.1f) {
        this.alpha = 0.0f
        return
    }
    this.animate().apply {
        interpolator = LinearInterpolator()
        duration = 500
        alpha(0f)
        withEndAction {
            visibility = endVisibility
        }
        start()
    }
}

private fun View.isParentAnimateLayoutChangesEnabled(): Boolean {
    val mParent = parent
    return mParent is ViewGroup && mParent.layoutAnimation != null
}
