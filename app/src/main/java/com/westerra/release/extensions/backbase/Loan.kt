package com.westerra.release.extensions.backbase

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.loan.Loan
import com.backbase.deferredresources.DeferredText
import com.westerra.release.constants.Constants.TYPE_NAME_AUTO
import com.westerra.release.constants.Constants.TYPE_NAME_LINE_OF_CREDIT
import com.westerra.release.constants.Constants.TYPE_NAME_LOC
import com.westerra.release.constants.Constants.TYPE_NAME_OVERDRAFT
import com.westerra.release.extensions.toDeferredText

fun Loan.isLineOfCreditOrOverdraftProduct(): Boolean {
    val name = productTypeName?.lowercase() ?: return false
    return name.contains(
        TYPE_NAME_LOC.lowercase(),
    ) || name.contains(
        TYPE_NAME_LINE_OF_CREDIT.lowercase(),
    ) || name.contains(
        TYPE_NAME_OVERDRAFT.lowercase(),
    )
}

fun Loan.isAutoProduct(): Boolean {
    val name = productTypeName?.lowercase() ?: return false
    return name.contains(TYPE_NAME_AUTO.lowercase())
}

fun Loan.showDMIPortal(): Boolean {
    return !isLineOfCreditOrOverdraftProduct() && !isAutoProduct()
}

fun Loan.getInterestRateDisplayText(): DeferredText? {
    return accountInterestRate?.toString()?.plus("%")?.toDeferredText()
}
