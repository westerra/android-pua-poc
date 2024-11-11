package com.westerra.release.accountandtransactions.toolbar

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.ToolbarMenuItem
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object NotificationsMenuItem {
    operator fun invoke(): ToolbarMenuItem {
        return ToolbarMenuItem {
            id = com.backbase.android.retail.journey.app.us.R.id.notifications
            title = com.westerra.release.R.string.notifications_title.toDeferredText()
            icon = com.backbase.engagementchannels.notifications.R.drawable.ic_bell_outline
                .toDeferredDrawable()
            showNotificationBadge = true
        }
    }
}
