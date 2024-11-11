package com.westerra.release.extensions.backbase

import com.backbase.android.business.journey.common.extensions.isToday
import com.backbase.android.retail.journey.payments.configuration.TextReviewState
import com.backbase.android.retail.journey.payments.form.model.PaymentData
import com.backbase.android.retail.journey.payments.model.PaymentPartyType
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredText

// disallow recurring option when 'from' account type is 'loan'
fun PaymentData.excludeRecurringOption(): Boolean {
    return isFromLoan() || isFromCreditCard()
}

fun PaymentData.isFromLoan(): Boolean {
    return fromParty?.paymentPartyType == PaymentPartyType.Loan
}

fun PaymentData.isFromCreditCard(): Boolean {
    return fromParty?.paymentPartyType == PaymentPartyType.CreditCard
}

// A recurring transfer's start date can't be today
fun PaymentData.invalidStartDateToday(): Boolean {
    return (paymentSchedule as? PaymentSchedule.Recurring)?.startDate?.isToday() == true
}

// A transfer from 'loan' or 'credit card' account has to be immediate and cannot be later or recurring.
// We have a customization to hide the recurring option so it's not possible to actually select Recurring.
fun PaymentData.invalidFutureSchedule(): Boolean {
    return (isFromLoan() || isFromCreditCard()) &&
        (paymentSchedule is PaymentSchedule.Later || paymentSchedule is PaymentSchedule.Recurring)
}

fun PaymentData.ratesFeesTextReviewState(): TextReviewState {
    return when {
        isFromCreditCard() ->
            TextReviewState.Visible(
                title = R.string.rates_fees_title.toDeferredText(),
                text = R.string.cash_advance_rules_fees_apply.toDeferredText(),
            )
        else -> TextReviewState.Invisible
    }
}

fun PaymentData.scheduleErrorsTextReviewState(): TextReviewState {
    return when {
        invalidStartDateToday() ->
            TextReviewState.Visible(
                title = R.string.error_title.toDeferredText(),
                text = R.string.choose_future_date_text.toDeferredText(),
            )
        invalidFutureSchedule() ->
            TextReviewState.Visible(
                title = R.string.error_title.toDeferredText(),
                text = R.string.future_transfer_load_cc_not_allowed.toDeferredText(),
            )
        else -> TextReviewState.Invisible
    }
}
