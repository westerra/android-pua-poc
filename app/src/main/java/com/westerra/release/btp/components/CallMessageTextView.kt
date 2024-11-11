package com.westerra.release.btp.components

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.alerts.ContactWesterraAlert

class CallMessageTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) :
    MaterialTextView(context, attrs) {
    companion object {
        private const val CONTACT_US_TEXT = "contact us"
    }

    init {
        movementMethod = LinkMovementMethod.getInstance()
    }

    // Convert certain substrings to be clickable and have a different color
    // "message" -> opens the messages journey
    // "call us" -> opens the dialer with the Westerra phone number
    override fun setText(text: CharSequence?, type: BufferType?) {
        if (text.isNullOrBlank()) {
            super.setText(text, type)
            return
        }
        val spannable = SpannableString(text)
        addLinkSpan(spannable = spannable, linkText = CONTACT_US_TEXT, action = {
            WesterraApplication.getInstance().getActivity()?.let {
                ContactWesterraAlert(activity = it, view = this).show()
            }
        })
        super.setText(spannable, BufferType.SPANNABLE)
    }

    private fun addLinkSpan(spannable: Spannable, linkText: String, action: Runnable) {
        val callClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                action.run()
            }
        }
        spannable.setSpan(
            callClickableSpan,
            spannable.indexOf(linkText, ignoreCase = true),
            spannable.indexOf(linkText, ignoreCase = true) + linkText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        context ?. let {
            spannable.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(it, R.color.aquaLink),
                ),
                spannable.indexOf(linkText, ignoreCase = true),
                spannable.indexOf(linkText, ignoreCase = true) + linkText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
    }
}
