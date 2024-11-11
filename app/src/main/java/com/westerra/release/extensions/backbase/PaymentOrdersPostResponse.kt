package com.westerra.release.extensions.backbase

import com.backbase.android.client.paymentorderclient2.model.PaymentOrdersPostResponse
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys

// Clear the last cached error message and save a new one if found.
fun PaymentOrdersPostResponse.cacheErrorMessage() {
    DataTransferCache().save(DataTransferCacheKeys.TRANSFER_ERROR_MESSAGE, "")
    this.getDisplayErrorMessage() ?. let {
        DataTransferCache().save(
            id = DataTransferCacheKeys.TRANSFER_ERROR_MESSAGE,
            obj = it,
        )
    }
}

fun PaymentOrdersPostResponse.getDisplayErrorMessage(): String? {
    val result = reasonText ?: errorDescription ?: ""
    // Filter out bad error messages from old api responses.
    if (Regex("^(\\d+ :).*").matches(result)) {
        return null
    }
    return result
}
