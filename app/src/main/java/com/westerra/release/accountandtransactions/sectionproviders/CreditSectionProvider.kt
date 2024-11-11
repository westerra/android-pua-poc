package com.westerra.release.accountandtransactions.sectionproviders

import com.backbase.android.retail.journey.accounts_and_transactions.account_details.CreditCardDetailsSectionsProvider
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.MaskableAttribute
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsRow
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsSection
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.credit_card.CreditCard
import com.westerra.release.R
import com.westerra.release.accountandtransactions.AccountTransactionsConfig.convertStringToCurrencyFormat
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toPreferredDateString

class CreditSectionProvider : CreditCardDetailsSectionsProvider {
    override fun provide(dto: CreditCard): List<AccountDetailsSection> {
        return listOf(
            AccountDetailsSection {
                title = R.string.balance_details_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        title = R.string.statement_closing_date_title.toDeferredText()
                        value = dto.lastUpdateDate.toPreferredDateString().toDeferredText()
                    },
                    AccountDetailsRow {
                        title = R.string.current_balance_title.toDeferredText()
                        value = dto.bookedBalance?.let {
                            convertStringToCurrencyFormat(
                                it,
                                dto.currency,
                            ).toDeferredText()
                        }
                    },
                    AccountDetailsRow {
                        title = R.string.available_credit_title.toDeferredText()
                        value = dto.availableBalance?.let {
                            convertStringToCurrencyFormat(
                                it,
                                dto.currency,
                            ).toDeferredText()
                        }
                    },
                    AccountDetailsRow {
                        title = R.string.credit_limit_title.toDeferredText()
                        value = dto.creditLimit?.let {
                            convertStringToCurrencyFormat(
                                it,
                                dto.currency,
                            ).toDeferredText()
                        }
                    },
                )
            },
            AccountDetailsSection {
                title = R.string.payment_details_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        title = R.string.minimum_payment_amount_due_title.toDeferredText()
                        value = dto.minimumPayment?.let {
                            convertStringToCurrencyFormat(
                                it.toString(),
                                dto.currency,
                            ).toDeferredText()
                        }
                    },
                    AccountDetailsRow {
                        title = R.string.next_payment_due_date_title.toDeferredText()
                        value = dto.minimumPaymentDueDate.toPreferredDateString().toDeferredText()
                    },
                )
            },
            AccountDetailsSection {
                title = R.string.interest_details_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        title = R.string.purchase_apr_title.toDeferredText()
                        value = dto.accountInterestRate?.toString()?.plus("%")?.toDeferredText()
                    },
                )
            },
            AccountDetailsSection {
                title = R.string.general_title.toDeferredText()
                rows = listOf(
                    AccountDetailsRow {
                        title = R.string.account_number_title.toDeferredText()
                        value = dto.number?.toDeferredText()
                        unmaskingAttributeName = MaskableAttribute.Number
                    },
                    AccountDetailsRow {
                        title = R.string.routing_number_title.toDeferredText()
                        value = Constants.WESTERRA_ROUTING_NUMBER.toDeferredText()
                    },
                    AccountDetailsRow {
                        title = R.string.account_type_title.toDeferredText()
                        value = dto.productTypeName?.toDeferredText()
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
    }
}
