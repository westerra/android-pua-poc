package com.westerra.release.accountandtransactions.sectionproviders

import android.content.Context
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.LoanDetailsSectionsProvider
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.MaskableAttribute
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.field_editor.ExternalActionConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsRow
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsSection
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.loan.Loan
import com.westerra.release.R
import com.westerra.release.accountandtransactions.AccountTransactionsConfig.convertStringToCurrencyFormat
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.backbase.getInterestRateDisplayText
import com.westerra.release.extensions.backbase.isLineOfCreditOrOverdraftProduct
import com.westerra.release.extensions.backbase.showDMIPortal
import com.westerra.release.extensions.fromLocalDateTimeFormatToPreferredDateString
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toPreferredDateString
import com.westerra.release.extensions.toUSDString
import com.westerra.release.sso.dmi.DMISSOExternalAction

class LoanSectionProvider(var context: Context) : LoanDetailsSectionsProvider {
    companion object {
        private const val KEY_MIN_PAYMENT_DUE_DATE = "minimumPaymentDueDate"
        private const val KEY_REMAINING_CREDIT = "remainingCredit"
        private const val KEY_CREDIT_LIMIT = "creditLimit"
    }

    override fun provide(dto: Loan): List<AccountDetailsSection> {
        val results = mutableListOf(
            AccountDetailsSection {
                title = R.string.balance_details_title.toDeferredText()
                rows = getBalanceDetails(dto = dto)
            },
            AccountDetailsSection {
                title = R.string.payment_details_title.toDeferredText()
                rows =
                    listOf(
                        AccountDetailsRow {
                            title = R.string.next_payment_amount_due_title.toDeferredText()
                            value = dto.monthlyInstalmentAmount?.toUSDString()?.toDeferredText()
                        },
                        AccountDetailsRow {
                            title = R.string.next_payment_due_date_title.toDeferredText()
                            value = dto.additions?.get(KEY_MIN_PAYMENT_DUE_DATE)
                                ?.fromLocalDateTimeFormatToPreferredDateString()?.toDeferredText()
                        },
                    )
            },
            AccountDetailsSection {
                title = R.string.loan_details_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        //
                        title = R.string.credit_limit_title.toDeferredText()
                        value = dto.additions?.get(KEY_CREDIT_LIMIT)?.let {
                            convertStringToCurrencyFormat(
                                it,
                                dto.currency,
                            ).toDeferredText()
                        }
                    },
                    AccountDetailsRow {
                        title = R.string.interest_rate_title.toDeferredText()
                        value = dto.getInterestRateDisplayText()
                    },
                )
            },
            AccountDetailsSection {
                title = R.string.general_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        title = R.string.account_number_title.toDeferredText()
                        value = dto.BBAN?.toDeferredText()
                        unmaskingAttributeName = MaskableAttribute.BBAN
                    },
                    AccountDetailsRow {
                        title = R.string.routing_number_title.toDeferredText()
                        value = Constants.WESTERRA_ROUTING_NUMBER.toDeferredText()
                    },
                    AccountDetailsRow {
                        title = R.string.account_type_title.toDeferredText()
                        value = dto.productKindName?.toDeferredText()
                    },
                    AccountDetailsRow {
                        title = R.string.account_opening_date_title.toDeferredText()
                        value = dto.accountOpeningDate.toPreferredDateString().toDeferredText()
                    },
                    AccountDetailsRow {
                        title = R.string.account_owners_title.toDeferredText()
                        value = dto.accountHolderNames?.toDeferredText()
                    },
                )
            },
        )
        if (dto.showDMIPortal()) {
            results.add(
                AccountDetailsSection {
                    title = R.string.other_title.toDeferredText()
                    rows = listOf(getMoreDetailsRow())
                },
            )
        }
        return results
    }

    private fun getMoreDetailsRow(): AccountDetailsRow {
        return AccountDetailsRow {
            title = R.string.more_details_title.toDeferredText()
            value = R.string.more_details_message.toDeferredText()
            externalActionConfigurations = listOf(getMoreDetailsAction())
        }
    }

    private fun getMoreDetailsAction(): ExternalActionConfiguration {
        return ExternalActionConfiguration {
            accessibilityLabel = R.string.more_details_message.toDeferredText()
            icon = R.drawable.info_circle.toDeferredDrawable()
            action = DMISSOExternalAction()
        }
    }

    private fun getBalanceDetails(dto: Loan): List<AccountDetailsRow> {
        return if (dto.isLineOfCreditOrOverdraftProduct()) {
            listOf(
                AccountDetailsRow {
                    title = R.string.current_balance_title.toDeferredText()
                    value =
                        dto.bookedBalance?.let {
                            convertStringToCurrencyFormat(
                                it,
                                dto.currency,
                            ).toDeferredText()
                        }
                },
                AccountDetailsRow {
                    title = R.string.available_credit_title.toDeferredText()
                    value =
                        dto.additions?.get(KEY_REMAINING_CREDIT).let {
                            convertStringToCurrencyFormat(it, dto.currency).toDeferredText()
                        }
                },
            )
        } else {
            listOf(
                AccountDetailsRow {
                    title = R.string.remaining_balance_title.toDeferredText()
                    value = dto.bookedBalance?.let {
                        convertStringToCurrencyFormat(
                            it,
                            dto.currency,
                        ).toDeferredText()
                    }
                },
            )
        }
    }
}
