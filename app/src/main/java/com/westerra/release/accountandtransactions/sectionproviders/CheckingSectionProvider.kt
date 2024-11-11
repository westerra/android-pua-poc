package com.westerra.release.accountandtransactions.sectionproviders

import com.backbase.android.retail.journey.accounts_and_transactions.account_details.CurrentAccountDetailsSectionsProvider
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.MaskableAttribute
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsRow
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.models.AccountDetailsSection
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.current_accounts.CurrentAccount
import com.westerra.release.R
import com.westerra.release.accountandtransactions.AccountTransactionsConfig.convertStringToCurrencyFormat
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.maskAccountNumber
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toPreferredDateString

class CheckingSectionProvider : CurrentAccountDetailsSectionsProvider {
    override fun provide(dto: CurrentAccount): List<AccountDetailsSection> {
        return listOf(
            AccountDetailsSection {
                title = R.string.balance_details_title.toDeferredText()
                rows =
                    listOf(
                        AccountDetailsRow {
                            title = R.string.available_balance_title.toDeferredText()
                            value =
                                dto.availableBalance?.let {
                                    convertStringToCurrencyFormat(it, dto.currency).toDeferredText()
                                }
                        },
                        AccountDetailsRow {
                            title = R.string.current_balance_title.toDeferredText()
                            value =
                                dto.bookedBalance?.let {
                                    convertStringToCurrencyFormat(it, dto.currency).toDeferredText()
                                }
                        },
                    )
            },
            AccountDetailsSection {
                title = R.string.general_title.toDeferredText()
                rows =
                    listOf(
                        AccountDetailsRow {
                            title = R.string.account_number_title.toDeferredText()
                            value = dto.BBAN?.maskAccountNumber()?.toDeferredText()
                            unmaskingAttributeName = MaskableAttribute.BBAN
                        },
                        AccountDetailsRow {
                            title = R.string.aba_routing_number_title.toDeferredText()
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
