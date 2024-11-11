package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants.WESTERRA_CALL_NUMBER
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.COMPOSE_MESSAGE_FIT_WINDOW_FLAG
import com.westerra.release.custom.DataTransferCacheKeys.COMPOSE_MESSAGE_PREFILL_TOPIC

class ContactWesterraAlert(activity: Activity, view: View) : AlertDialog.Builder(activity) {
    init {
        setCancelable(true)
        setTitle(R.string.contact_westerra)
        setPositiveButton(R.string.message) { _, _ ->
            DataTransferCache().save(
                COMPOSE_MESSAGE_PREFILL_TOPIC,
                context.getText(R.string.open_new_account),
            )
            DataTransferCache().save(
                COMPOSE_MESSAGE_FIT_WINDOW_FLAG,
                true,
            )
            view.findNavController().navigate(
                R.id.action_btp_to_composeMessageScreen,
                bundleOf(),
            )
        }
        setNegativeButton(R.string.call) { _, _ ->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$WESTERRA_CALL_NUMBER")
            WesterraApplication.getInstance().getActivity()?.startActivity(intent)
        }
        setNeutralButton(R.string.cancel) { dialog, _ ->
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
