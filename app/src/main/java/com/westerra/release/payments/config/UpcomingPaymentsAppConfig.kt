package com.westerra.release.payments.config

import com.backbase.android.retail.journey.payments.upcoming.UpcomingPaymentsAppConfiguration
import com.backbase.android.retail.journey.payments.upcoming.view.list.UpcomingPaymentScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredText

object UpcomingPaymentsAppConfig {

    operator fun invoke(): UpcomingPaymentsAppConfiguration {
        return UpcomingPaymentsAppConfiguration {
            title = R.string.transfer_activity_title.toDeferredText()
            upcomingPaymentScreen = UpcomingPaymentScreenConfiguration {
                scheduledTabText = R.string.upcoming_title.toDeferredText()
            }
        }
    }
}
