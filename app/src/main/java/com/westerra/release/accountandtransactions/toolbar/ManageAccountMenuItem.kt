package com.westerra.release.accountandtransactions.toolbar

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.ToolbarMenuItem
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object ManageAccountMenuItem {
    operator fun invoke(): ToolbarMenuItem {
        return ToolbarMenuItem {
            id = com.backbase.android.retail.journey.app.us.R.id.manageAccount
            title = R.string.edit_accounts_title.toDeferredText()
            icon = com.backbase.engagementchannels.notifications.R.drawable.ic_settings
                .toDeferredDrawable()
            showNotificationBadge = false
        }
    }
}
