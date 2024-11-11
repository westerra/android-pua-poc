package com.westerra.release.messages.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.COMPOSE_MESSAGE_FIT_WINDOW_FLAG

class CustomFittingConstraintLayout
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    init {
        fitsSystemWindows =
            DataTransferCache().retrieve(COMPOSE_MESSAGE_FIT_WINDOW_FLAG) as? Boolean ?: false
        DataTransferCache().save(COMPOSE_MESSAGE_FIT_WINDOW_FLAG, false)
    }
}
