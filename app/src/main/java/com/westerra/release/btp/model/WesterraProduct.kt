package com.westerra.release.btp.model

import android.content.Context
import com.westerra.release.R
import com.westerra.release.btp.fragments.BtpIntroFragment
import com.westerra.release.constants.Constants
import java.text.NumberFormat

data class WesterraProduct(
    val typeId: String?,
    val type: String?,
    val category: ProductCardType?,
    val productMarketingUrl: String?,
    val externalMarketingUrl: String?,
    val maxAllowedAccounts: Int?,
    val minimumBalanceRequired: Int?,
    var currentAccountCount: Int = 0,
) : java.io.Serializable, Comparable<WesterraProduct> {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )

    companion object {
        const val PRODUCT_MARKETING_TEST_URL = "https://westerra-dev.vercel.app/iframe-test"
        val SPENDING_HEADER = WesterraProduct(
            typeId = null,
            type = "spending",
            category = ProductCardType.HEADER,
            productMarketingUrl = null,
            externalMarketingUrl = null,
            maxAllowedAccounts = null,
            minimumBalanceRequired = null,
        )
        val SAVINGS_HEADER = WesterraProduct(
            typeId = null,
            type = "savings",
            category = ProductCardType.HEADER,
            productMarketingUrl = null,
            externalMarketingUrl = null,
            maxAllowedAccounts = null,
            minimumBalanceRequired = null,
        )
        val FOOTER = WesterraProduct(
            typeId = null,
            type = null,
            category = ProductCardType.FOOTER,
            productMarketingUrl = null,
            externalMarketingUrl = null,
            maxAllowedAccounts = null,
            minimumBalanceRequired = null,
        )
    }

    override fun toString(): String {
        return "WesterraProduct{typeId: $typeId, " +
            "type: $type, " +
            "category: $category, " +
            "productMarketingUrl: $productMarketingUrl, " +
            "externalMarketingUrl: $externalMarketingUrl, " +
            "maxAllowedAccounts: $maxAllowedAccounts, " +
            "minimumBalanceRequired: $minimumBalanceRequired" +
            "}"
    }

    override fun compareTo(other: WesterraProduct): Int {
        if (category == null && other.category == null) return compareDisplayNames(other = other)
        if (isSpendingType() && other.isSpendingType()) return 0
        if (isSpendingType()) return -1
        if (other.isSpendingType()) return 1
        if (isSavingsType() && other.isSavingsType()) return 0
        if (isSavingsType()) return -1
        if (other.isSavingsType()) return 1
        return compareDisplayNames(other = other)
    }

    private fun compareDisplayNames(other: WesterraProduct): Int {
        return getDisplayName().compareTo(other.getDisplayName())
    }

    fun getDisplayName(): String {
        return type ?: ""
    }

    fun getHeaderText(): String {
        return type ?: ""
    }

    fun getMoreInfoUrl(): String {
        externalMarketingUrl ?. let {
            return it
        }
        return when (category) {
            ProductCardType.CHECKING -> Constants.WESTERRA_SPENDING_URL
            ProductCardType.CREDIT_CARD -> Constants.WESTERRA_SPENDING_URL
            ProductCardType.SAVINGS -> Constants.WESTERRA_SAVINGS_URL
            ProductCardType.CD -> Constants.WESTERRA_SAVINGS_URL
            else -> Constants.WESTERRA_HOME_URL
        }
    }

    fun isSpendingType(): Boolean {
        return when (category) {
            ProductCardType.CHECKING -> true
            ProductCardType.CREDIT_CARD -> true
            else -> false
        }
    }

    fun isSavingsType(): Boolean {
        return when (category) {
            ProductCardType.SAVINGS -> true
            ProductCardType.CD -> true
            else -> false
        }
    }

    fun getMinimumRequirementDisplayAmount(): String {
        return NumberFormat.getCurrencyInstance().format(getMinimumRequirementAmount())
    }

    fun areItemsSame(other: WesterraProduct): Boolean {
        return typeId == other.typeId
    }

    fun areContentsSame(other: WesterraProduct): Boolean {
        return (type == other.type && category == other.category)
    }

    fun hasMinimumAmountRequirement(): Boolean {
        minimumBalanceRequired ?. let {
            return true
        }
        return false
    }

    fun getMinimumRequirementAmount(): Int {
        return minimumBalanceRequired ?: 0
    }

    fun isFilterType(filter: String?): Boolean {
        if (filter == null) return true
        if (filter == BtpIntroFragment.SPENDING_FILTER) return isSpendingType()
        if (filter == BtpIntroFragment.SAVINGS_FILTER) return isSavingsType()
        return false
    }

    fun getProductMarketingMarketingUrl(): String {
        return productMarketingUrl ?: PRODUCT_MARKETING_TEST_URL
    }

    fun isEligible(): Boolean {
        return currentAccountCount < (maxAllowedAccounts ?: 0)
    }

    fun getNotEligibleReason(context: Context): String {
        if (maxAllowedAccounts == 1 && currentAccountCount >= 1) {
            return context.getString(R.string.you_have_an_account)
        } else if (maxAllowedAccounts in 2..currentAccountCount) {
            return context.getString(R.string.you_have_reached_account_limit)
        }
        return context.getString(R.string.you_are_not_eligible)
    }
}
