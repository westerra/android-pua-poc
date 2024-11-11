package com.westerra.release.payments.config

import com.backbase.android.retail.journey.payments.PaymentJourneyConfiguration
import com.backbase.android.retail.journey.payments.configuration.AmountInput
import com.backbase.android.retail.journey.payments.configuration.AmountReview
import com.backbase.android.retail.journey.payments.configuration.BalanceConfiguration
import com.backbase.android.retail.journey.payments.configuration.Completion
import com.backbase.android.retail.journey.payments.configuration.ContactSelection
import com.backbase.android.retail.journey.payments.configuration.Custom
import com.backbase.android.retail.journey.payments.configuration.Form
import com.backbase.android.retail.journey.payments.configuration.FrequencyOption
import com.backbase.android.retail.journey.payments.configuration.FromPartySelection
import com.backbase.android.retail.journey.payments.configuration.PaymentDescriptionReview
import com.backbase.android.retail.journey.payments.configuration.PaymentPartyReview
import com.backbase.android.retail.journey.payments.configuration.RemittanceInfoInput
import com.backbase.android.retail.journey.payments.configuration.Review
import com.backbase.android.retail.journey.payments.configuration.SavingsAccountBalanceType
import com.backbase.android.retail.journey.payments.configuration.ScheduleReview
import com.backbase.android.retail.journey.payments.configuration.ScheduleSelector
import com.backbase.android.retail.journey.payments.configuration.TextReview
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import com.westerra.release.R
import com.westerra.release.extensions.backbase.excludeRecurringOption
import com.westerra.release.extensions.backbase.ratesFeesTextReviewState
import com.westerra.release.extensions.backbase.scheduleErrorsTextReviewState
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.payments.CustomPaymentFragment
import java.util.Currency
import java.util.Locale

object ContactsP2PConfig {
    private const val PAYMENT_TYPE_INTRABANK_TRANSFER = "INTRABANK_TRANSFER"
    private val STEPS =
        listOf(
            ContactSelection {},
            FromPartySelection {
                bottomActionText = R.string.continue_button_text.toDeferredText()
                listPageSize = 100
            },
            Form {
                title = R.string.payment_details_title.toDeferredText()
                bottomActionText = R.string.continue_review_button_text.toDeferredText()
                fields =
                    listOf(
                        AmountInput {},
                        ScheduleSelector {
                            scheduleHeader = R.string.when_title.toDeferredText()
                            excludeRecurringOption = { paymentData ->
                                paymentData.excludeRecurringOption()
                            }
                        },
                        RemittanceInfoInput {
                            title = R.string.note_title.toDeferredText()
                            placeHolderText = R.string.enter_note_placeholder_text.toDeferredText()
                        },
                    )
            },
            Review {
                bottomActionTextImmediate = R.string.confirm_transfer_button_text.toDeferredText()
                bottomActionTextLater = R.string.continue_review_button_text.toDeferredText()
                fields =
                    listOf(
                        AmountReview {},
                        PaymentPartyReview {
                            toAccountNumberVisible = true
                        },
                        ScheduleReview {},
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
                    InternalA2AConfig.createCompleteScreenConfig(
                        paymentOrderResponse = paymentOrderResponse,
                    )
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

    operator fun invoke(): PaymentJourneyConfiguration {
        return PaymentJourneyConfiguration(Currency.getInstance(Locale.US)) {
            paymentType = PAYMENT_TYPE_INTRABANK_TRANSFER
            defaultAccountIcon = R.drawable.current_icon_small.toDeferredDrawable()
            balanceConfiguration =
                BalanceConfiguration {
                    savingsAccountBalanceType = SavingsAccountBalanceType.AvailableBalance()
                }
            filteredFrequencyOptions = { _ ->
                setOf(
                    FrequencyOption.Weekly(),
                    FrequencyOption.Biweekly(),
                    FrequencyOption.Monthly(),
                    FrequencyOption.Quarterly(),
                    FrequencyOption.Yearly(),
                )
            }
            filteredRecurringEndingOptions = { _ ->
                setOf(
                    PaymentSchedule.Recurring.Ending.Never,
                    PaymentSchedule.Recurring.Ending.After(occurrences = 1),
                )
            }
            steps = STEPS
        }
    }
}
