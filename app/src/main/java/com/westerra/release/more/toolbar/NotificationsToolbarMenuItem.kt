package com.westerra.release.more.toolbar

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.MoreToolbarMenuItem
import com.backbase.android.retail.journey.more.OnActionComplete
import com.backbase.engagementchannels.notifications.R
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

class NotificationsToolbarMenuItem {
    val item =
        MoreToolbarMenuItem {
            icon = R.drawable.ic_bell_outline.toDeferredDrawable()
            title = com.westerra.release.R.string.notifications_title.toDeferredText()
            onMoreToolbarMenuItemSelected = {
                OnActionComplete.NavigateTo(
                    com.westerra.release.R.id.action_mainScreen_to_notificationsJourney,
                    bundleOf(),
                )
            }
        }
}
