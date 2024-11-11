package com.westerra.release.profile.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AutoFormatProfileTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    companion object {
        private const val PRIMARY_KEY = " (Primary)"
    }

    // Remove unwanted "Primary" text when set.
    override fun setText(text: CharSequence?, type: BufferType?) {
        var result = text
        text ?. let { txt ->
            if (txt.endsWith(PRIMARY_KEY)) {
                result = txt.removeSuffix(PRIMARY_KEY)
            }
        }
        super.setText(result, type)
    }
}
