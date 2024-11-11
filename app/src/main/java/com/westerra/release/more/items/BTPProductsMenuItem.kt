package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.analytics.WesterraAnalytics

class BTPProductsMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.btp_title,
        icon = R.drawable.westerra_w_logo_white_padded,
        actionBlock = {
            WesterraAnalytics.recordBtpStartEvent()
            OnActionComplete.NavigateTo(
                R.id.action_bottomMenuScreen_to_btp,
                bundleOf(),
            )
        },
    )
}
