package com.westerra.release.extensions.backbase

import com.backbase.android.retail.journey.payments.form.model.PaymentData
import com.backbase.android.retail.journey.payments.model.Amount
import com.backbase.android.retail.journey.payments.model.Balance
import com.backbase.android.retail.journey.payments.model.IdentificationType
import com.backbase.android.retail.journey.payments.model.PaymentParty
import com.backbase.android.retail.journey.payments.model.PaymentPartyType
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import java.math.BigDecimal
import java.time.LocalDate
import org.junit.Assert
import org.junit.Test

class PaymentDataTest {
    private val fromLoanParty =
        PaymentParty(
            id = "fromParty",
            name = "fromName",
            identifications = listOf(IdentificationType.AccountNumber(identification = "12345")),
            availableFunds = Balance(
                availableBalance = BigDecimal.TEN,
                bookedBalance = null,
                remainingCredit = null,
            ),
            paymentPartyType = PaymentPartyType.Loan,
            currencyCode = "USD",
            amountOptions = null,
        )
    private val fromCreditCardParty =
        PaymentParty(
            id = "fromParty",
            name = "fromName",
            identifications = listOf(IdentificationType.AccountNumber(identification = "12345")),
            availableFunds = Balance(
                availableBalance = BigDecimal.TEN,
                bookedBalance = null,
                remainingCredit = null,
            ),
            paymentPartyType = PaymentPartyType.CreditCard,
            currencyCode = "USD",
            amountOptions = null,
        )
    private val toParty =
        PaymentParty(
            id = "toParty",
            name = "toName",
            identifications = listOf(IdentificationType.AccountNumber(identification = "55555")),
            availableFunds = Balance(
                availableBalance = BigDecimal.TEN,
                bookedBalance = null,
                remainingCredit = null,
            ),
            paymentPartyType = PaymentPartyType.Loan,
            currencyCode = "USD",
            amountOptions = null,
        )
    private val recurringPaymentScheduleStartToday =
        PaymentSchedule.Recurring(
            startDate = LocalDate.now(),
            end = PaymentSchedule.Recurring.Ending.Never,
            transferFrequency = PaymentSchedule.Recurring.TransferFrequency.Weekly,
        )
    private val recurringPaymentScheduleStartTomorrow =
        PaymentSchedule.Recurring(
            startDate = LocalDate.now().plusDays(1),
            end = PaymentSchedule.Recurring.Ending.Never,
            transferFrequency = PaymentSchedule.Recurring.TransferFrequency.Weekly,
        )
    private val schedulePaymentScheduleStartToday =
        PaymentSchedule.Later(
            requestedExecutionDate = LocalDate.now(),
        )
    private val schedulePaymentScheduleStartTomorrow =
        PaymentSchedule.Later(
            requestedExecutionDate = LocalDate.now().plusDays(1L),
        )

    private val paymentDataRecurringFromLoan =
        PaymentData(
            fromParty = fromLoanParty,
            toParty = toParty,
            amount = Amount(BigDecimal.ONE, currencyCode = "USD"),
            paymentSchedule = recurringPaymentScheduleStartToday,
            memo = "Unit test from loan",
        )
    private val paymentDataRecurringFromCreditCard =
        PaymentData(
            fromParty = fromCreditCardParty,
            toParty = toParty,
            amount = Amount(BigDecimal.ONE, currencyCode = "USD"),
            paymentSchedule = recurringPaymentScheduleStartTomorrow,
            memo = "Unit test from credit card",
        )
    private val paymentDataScheduleTodayFromLoan =
        PaymentData(
            fromParty = fromLoanParty,
            toParty = toParty,
            amount = Amount(BigDecimal.ONE, currencyCode = "USD"),
            paymentSchedule = schedulePaymentScheduleStartToday,
            memo = "Unit test from loan",
        )
    private val paymentDataScheduleTomorrowFromLoan =
        PaymentData(
            fromParty = fromLoanParty,
            toParty = toParty,
            amount = Amount(BigDecimal.ONE, currencyCode = "USD"),
            paymentSchedule = schedulePaymentScheduleStartTomorrow,
            memo = "Unit test from loan",
        )

    @Test
    fun paymentData_excludeRecurringOption_test() {
        Assert.assertTrue(paymentDataRecurringFromLoan.excludeRecurringOption())
        Assert.assertTrue(paymentDataRecurringFromCreditCard.excludeRecurringOption())
        Assert.assertTrue(paymentDataScheduleTodayFromLoan.excludeRecurringOption())
        Assert.assertTrue(paymentDataScheduleTomorrowFromLoan.excludeRecurringOption())
    }

    @Test
    fun paymentData_isFromLoan_test() {
        Assert.assertTrue(paymentDataRecurringFromLoan.isFromLoan())
        Assert.assertFalse(paymentDataRecurringFromCreditCard.isFromLoan())
        Assert.assertTrue(paymentDataScheduleTodayFromLoan.isFromLoan())
        Assert.assertTrue(paymentDataScheduleTomorrowFromLoan.isFromLoan())
    }

    @Test
    fun paymentData_invalidStartDateToday_test() {
        Assert.assertFalse(paymentDataScheduleTodayFromLoan.invalidStartDateToday())
        Assert.assertFalse(paymentDataScheduleTomorrowFromLoan.invalidStartDateToday())
        Assert.assertTrue(paymentDataRecurringFromLoan.invalidStartDateToday())
        Assert.assertFalse(paymentDataRecurringFromCreditCard.invalidStartDateToday())
    }

    @Test
    fun paymentData_invalidFutureSchedule_test() {
        Assert.assertTrue(paymentDataScheduleTodayFromLoan.invalidFutureSchedule())
        Assert.assertTrue(paymentDataScheduleTomorrowFromLoan.invalidFutureSchedule())
        Assert.assertTrue(paymentDataRecurringFromLoan.invalidFutureSchedule())
        Assert.assertTrue(paymentDataRecurringFromCreditCard.invalidFutureSchedule())
    }
}
