package com.westerra.release.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.backbase.android.retail.journey.accounts_and_transactions.common.DeferredTextAppearance
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText

internal fun @receiver:StringRes Int.resourceString(context: Context): String {
    return context.resources.getString(this)
}

fun @receiver:StringRes Int.toDeferredText(): DeferredText {
    return DeferredText.Resource(this)
}

fun @receiver:DrawableRes Int.toDeferredDrawable(
    transformations: Drawable.(Context) -> Unit = {
    },
): DeferredDrawable.Resource {
    return DeferredDrawable.Resource(resId = this, transformations = transformations)
}

fun @receiver:ColorRes Int.toDeferredColor(): DeferredColor {
    return DeferredColor.Resource(this)
}

fun @receiver:StyleRes Int.toDeferredTextAppearance(): DeferredTextAppearance {
    return DeferredTextAppearance.Constant(this)
}
