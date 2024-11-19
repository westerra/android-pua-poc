package com.westerra.release.payments.config

import com.backbase.android.retail.journey.payments.PaymentJourneyConfiguration
import com.backbase.android.retail.journey.payments.configuration.AmountInput
import com.backbase.android.retail.journey.payments.configuration.AmountReview
import com.backbase.android.retail.journey.payments.configuration.BalanceConfiguration
import com.backbase.android.retail.journey.payments.configuration.Completion
import com.backbase.android.retail.journey.payments.configuration.CompletionScreenConfiguration
import com.backbase.android.retail.journey.payments.configuration.Custom
import com.backbase.android.retail.journey.payments.configuration.Form
import com.backbase.android.retail.journey.payments.configuration.FrequencyOption
import com.backbase.android.retail.journey.payments.configuration.FromPartySelection
import com.backbase.android.retail.journey.payments.configuration.PaymentDescriptionReview
import com.backbase.android.retail.journey.payments.configuration.PaymentPartyReview
import com.backbase.android.retail.journey.payments.configuration.PaymentPartySelector
import com.backbase.android.retail.journey.payments.configuration.RemittanceInfoInput
import com.backbase.android.retail.journey.payments.configuration.Review
import com.backbase.android.retail.journey.payments.configuration.SavingsAccountBalanceType
import com.backbase.android.retail.journey.payments.configuration.ScheduleReview
import com.backbase.android.retail.journey.payments.configuration.ScheduleSelector
import com.backbase.android.retail.journey.payments.configuration.ScheduleSelectorSections
import com.backbase.android.retail.journey.payments.configuration.TextReview
import com.backbase.android.retail.journey.payments.configuration.ToPartySelection
import com.backbase.android.retail.journey.payments.model.PaymentOrderResponse
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import com.backbase.android.retail.journey.payments.model.Status
import com.backbase.deferredresources.DeferredText
import com.westerra.release.R
import com.westerra.release.extensions.backbase.ratesFeesTextReviewState
import com.westerra.release.extensions.backbase.scheduleErrorsTextReviewState
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.payments.CustomPaymentFragment
import java.util.Currency
import java.util.Locale

object InternalA2AConfig {

    private const val PAYMENT_TYPE_INTERNAL_TRANSFER = "INTERNAL_TRANSFER"

    operator fun invoke(): PaymentJourneyConfiguration {
        return PaymentJourneyConfiguration(Currency.getInstance(Locale.US)) {
            paymentType = PAYMENT_TYPE_INTERNAL_TRANSFER
            defaultAccountIcon = R.drawable.current_icon_small.toDeferredDrawable()
            //TODO - balanceConfiguration: BalanceConfiguration' is deprecated. Use paymentPartyBalanceConfiguration instead.
//            balanceConfiguration = BalanceConfiguration {
//                savingsAccountBalanceType = SavingsAccountBalanceType.AvailableBalance()
//            }
            paymentPartyBalanceConfiguration = { _, _ ->
                BalanceConfiguration {
                    savingsAccountBalanceType = SavingsAccountBalanceType.AvailableBalance()
                }
            }

            // sets the list of allowed frequency types in recurring payment
            filteredFrequencyOptions = { _ ->
                setOf(
                    FrequencyOption.Weekly(),
                    FrequencyOption.Biweekly(),
                    FrequencyOption.Monthly(),
                    FrequencyOption.Quarterly(),
                    FrequencyOption.Yearly(),
                )
            }
            steps = listOf(
                Form {
                    title = R.string.make_transfer_title.toDeferredText()
                    bottomActionText = R.string.continue_review_button_text.toDeferredText()
                    fields = listOf(
                        PaymentPartySelector {
                            fromPartySelector = FromPartySelection {
                                listPageSize = 100
                            }
                            toPartySelector = ToPartySelection {
                                listPageSize = 100
                            }
                        },
                        AmountInput {},
                        ScheduleSelector {
                            scheduleHeader = R.string.when_title.toDeferredText()

                            //TODO - excludeRecurringOption: (paymentData: PaymentData) -> Boolean' is deprecated. Use `sections` to show or hide sections instead.
//                            excludeRecurringOption = { paymentData ->
//                                paymentData.excludeRecurringOption()
//                            }
                            sections = {
                                mutableSetOf(
                                    ScheduleSelectorSections.Immediate.Builder().build(),
                                    ScheduleSelectorSections.LaterDate.Builder().apply{}.build(),
                                    //ScheduleSelectorSections.Recurring.Builder().apply{}.build()
                                )
                            }
                        },
                        RemittanceInfoInput {
                            title = R.string.note_title.toDeferredText()
                            placeHolderText = R.string.enter_note_placeholder_text.toDeferredText()
                        },
                    )
                },
                Review {
                    bottomActionTextImmediate =
                        R.string.confirm_transfer_button_text.toDeferredText()
                    bottomActionTextLater = R.string.continue_review_button_text.toDeferredText()
                    fields = listOf(
                        AmountReview {},
                        PaymentPartyReview {},
                        ScheduleReview {
                            scheduleNowDescription =
                                R.string.transfer_executed_immediate_message.toDeferredText()
                        },
                        TextReview { paymentData ->
                            paymentData.scheduleErrorsTextReviewState()
                        },
                        TextReview { paymentData ->
                            paymentData.ratesFeesTextReviewState()
                        },
                        PaymentDescriptionReview {},
                    )
                },
                Completion {
                    bottomActionText = R.string.done_button_text.toDeferredText()
                    onComplete = { paymentOrderResponse ->
                        createCompleteScreenConfig(paymentOrderResponse = paymentOrderResponse)
                    }
                },
                // the below custom fragment is placed to make "PaymentCompleteNavigationAction"
                // work, which overrides bottom action button
                Custom<CustomPaymentFragment> {
                    customLaunchCondition = {
                        true
                    }
                },
            )
        }
    }

    internal fun createCompleteScreenConfig(
        paymentOrderResponse: PaymentOrderResponse,
    ): CompletionScreenConfiguration {
        // list of success payment statuses
        val successPaymentStatus =
            listOf(Status.Accepted, Status.Processed, Status.Entered, Status.Ready, Status.Draft)
        // list of failure payment statuses
        // val failurePaymentStatus = listOf(Status.Rejected, Status.Cancelled,
        // Status.CancellationPending, Status.ConfirmationPending, Status.ConfirmationDeclined)

        return if (successPaymentStatus.contains(paymentOrderResponse.status)) {
            if (paymentOrderResponse.paymentOrder?.paymentSchedule !is PaymentSchedule.Immediate) {
                // schedule payment case
                CompletionScreenConfiguration {
                    title = R.string.transfer_scheduled_title.toDeferredText()
                    image = R.drawable.ic_success_check.toDeferredDrawable()
                    message = R.string.transfer_scheduled_message.toDeferredText()
                }
            } else {
                // success case
                CompletionScreenConfiguration {
                    title = R.string.transfer_done_title.toDeferredText()
                    image = R.drawable.ic_success_check.toDeferredDrawable()
                    message = R.string.transfer_completed_message.toDeferredText()
                }
            }
        } else {
            // failure case
            CompletionScreenConfiguration {
                title = R.string.transfer_rejected_title.toDeferredText()
                image = R.drawable.ic_payment_failed.toDeferredDrawable()
                message = DeferredText.Resource(
                    R.string.transfer_rejected_message,
                    DeferredText.Resource.Type.TEXT,
                )
            }
        }
    }
}
