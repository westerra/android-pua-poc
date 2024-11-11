package com.westerra.release.messages.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.COMPOSE_MESSAGE_PREFILL_TOPIC

class PrefillAutoCompleteTextView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatAutoCompleteTextView(context, attrs) {
    init {
        val prefillTopic = DataTransferCache().retrieve(COMPOSE_MESSAGE_PREFILL_TOPIC) as? String
        if (!prefillTopic.isNullOrBlank()) {
            setText(prefillTopic)
            DataTransferCache().save(COMPOSE_MESSAGE_PREFILL_TOPIC, "")
        }
    }
}
