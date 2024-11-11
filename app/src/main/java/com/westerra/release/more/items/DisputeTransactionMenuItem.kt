package com.westerra.release.more.items

import android.os.Looper
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants.DISPUTE_TRANSACTION_URL

class DisputeTransactionMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.dispute_transaction,
        icon = R.drawable.backbase_ic_gavel,
        actionBlock = {
            if (Looper.getMainLooper().isCurrentThread) {
                WesterraApplication.getInstance()
                    .launchExternalBrowser(url = DISPUTE_TRANSACTION_URL)
            }
            OnActionComplete.DoNothing
        },
    )
}
