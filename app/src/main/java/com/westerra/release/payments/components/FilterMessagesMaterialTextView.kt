package com.westerra.release.payments.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textview.MaterialTextView

class FilterMessagesMaterialTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : MaterialTextView(context, attrs) {
    companion object {
        private const val INVALID_MESSAGE_1 = "There is no amount due on this Credit Card"
    }

    // Prevent showing invalid messages to user.
    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(filterInvalidText(text), type)
    }

    private fun filterInvalidText(text: CharSequence?): CharSequence? {
        if (!isValid(text)) {
            postGone()
            return ""
        }
        return text
    }

    private fun isValid(text: CharSequence?): Boolean {
        if (text == null) return true
        when (text) {
            INVALID_MESSAGE_1 ->
                return false
        }
        return true
    }

    // Likely Backbase sets visibility after this setText call,
    // so need to delay execution to happen after that.
    private fun postGone() {
        post {
            this.visibility = View.GONE
        }
    }
}
