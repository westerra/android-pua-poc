package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.westerra.release.R
import com.westerra.release.btp.model.WesterraProduct
import java.util.Locale

class ProductConfirmationAlert(
    product: WesterraProduct,
    activity: Activity,
    onConfirm: () -> Unit,
) : AlertDialog.Builder(activity) {
    init {
        setCancelable(true)
        setTitle(R.string.please_confirm)
        val message = String.format(
            Locale.getDefault(),
            activity.getString(R.string.product_confirm_message_format),
            product.getDisplayName(),
        )
        setMessageFormatted(message = message)
        setPositiveButton(R.string.ok) { _, _ ->
            onConfirm()
        }
        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun setMessageFormatted(message: String) {
        val messageSpan = SpannableString(message)
        val firstQuoteIndex = message.indexOf('"')
        val secondQuoteIndex = message.indexOf('"', startIndex = firstQuoteIndex + 1) + 1
        if (firstQuoteIndex > 0 &&
            secondQuoteIndex > 0 &&
            firstQuoteIndex < secondQuoteIndex &&
            secondQuoteIndex < messageSpan.length
        ) {
            messageSpan.setSpan(
                StyleSpan(Typeface.BOLD),
                firstQuoteIndex,
                secondQuoteIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        setMessage(messageSpan)
    }
}
