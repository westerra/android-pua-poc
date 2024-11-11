package com.westerra.release.more.items

import android.os.Looper
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants.SCHEDULE_APPOINTMENT_URL

class ScheduleAppointmentMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.schedule_appointment,
        icon = R.drawable.ic_pending,
        actionBlock = {
            if (Looper.getMainLooper().isCurrentThread) {
                WesterraApplication.getInstance()
                    .launchExternalBrowser(url = SCHEDULE_APPOINTMENT_URL)
            }
            OnActionComplete.DoNothing
        },
    )
}
