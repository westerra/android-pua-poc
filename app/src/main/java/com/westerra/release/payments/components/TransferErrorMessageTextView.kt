package com.westerra.release.payments.components

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.westerra.release.R
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys

class TransferErrorMessageTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : MaterialTextView(context, attrs) {

    override fun setText(text: CharSequence?, type: BufferType?) {
        DataTransferCache().retrieve(DataTransferCacheKeys.TRANSFER_ERROR_MESSAGE) ?. let {
            if (it is String && it.isNotBlank()) {
                val finalMessage = it + context.getString(R.string.transfer_error_message_suffix)
                super.setText(finalMessage, type)
                return
            }
        }
        super.setText(text, type)
    }
}
