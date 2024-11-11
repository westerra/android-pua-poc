package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.backbase.engagementchannels.notifications.NotificationsJourney
import com.westerra.release.R

class ManageNotificationsMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.manage_notifications_title,
        icon = R.drawable.ic_notifications,
        actionBlock = {
            OnActionComplete.NavigateTo(
                R.id.action_mainScreen_to_notificationsJourney,
                bundleOf(NotificationsJourney.LAUNCH_ACTION_NOTIFICATION_SETTINGS to true),
            )
        },
    )
}
