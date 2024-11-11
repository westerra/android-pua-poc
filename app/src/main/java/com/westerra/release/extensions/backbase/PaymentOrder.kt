package com.westerra.release.extensions.backbase

import com.backbase.android.retail.journey.payments.model.PaymentOrder
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import com.westerra.release.extensions.getCurrentLocalDate
import java.time.LocalDate

fun PaymentOrder.requestedExecutionDate(): LocalDate {
    return when (paymentSchedule) {
        is PaymentSchedule.Immediate -> getCurrentLocalDate()
        is PaymentSchedule.Recurring -> (paymentSchedule as PaymentSchedule.Recurring).startDate
        is PaymentSchedule.Later ->
            (paymentSchedule as PaymentSchedule.Later).requestedExecutionDate
        else -> getCurrentLocalDate()
    }
}
