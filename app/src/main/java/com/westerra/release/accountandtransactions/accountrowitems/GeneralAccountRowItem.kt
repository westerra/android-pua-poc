package com.westerra.release.accountandtransactions.accountrowitems

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountRowItem
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.custom_products.GeneralAccount
import com.westerra.release.R
import com.westerra.release.accountandtransactions.AccountTransactionsConfig
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.maskAccountNumber
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toDeferredTextAppearance

object GeneralAccountRowItem {
    operator fun invoke(dto: GeneralAccount): AccountRowItem {
        return AccountRowItem {
            accountId = dto.id
            title = dto.displayName
            subtitle = dto.BBAN?.maskAccountNumber()
            titleStyle = R.style.AccountRowTitleStyle.toDeferredTextAppearance()
            subtitleStyle = R.style.AccountRowSubTitleStyle.toDeferredTextAppearance()
            rightAccessoryText =
                dto.bookedBalance?.let {
                    AccountTransactionsConfig.convertStringToCurrencyFormat(it, dto.currency)
                }
            rightAccessoryTextStyle =
                R.style.AccountRowRightAccessoryTextStyle.toDeferredTextAppearance()
            if (dto.state?.state != Constants.STATE_ACTIVE) {
                rightAccessorySubtitle = R.string.account_closed_subtitle.toDeferredText()
            }
        }
    }
}
