package com.westerra.release.btp.model

import android.util.Log

data class BtpProductItem(
    val additions: BtpAdditionItem?,
    val id: String?,
    val name: String?,
    val displayName: String?,
    val crossCurrencyAllowed: Boolean?,
    val productKindName: String?,
    val productTypeName: String?,
    val bankAlias: String?,
    val sourceId: String?,
    val visible: Boolean?,
    val accountOpeningDate: String?,
    val lastUpdateDate: String?,
    val state: BtpStateItem?,
    val bookedBalance: String?,
    val availableBalance: String?,
    val BBAN: String?,
    val currency: String?,
    val bankBranchCode: String?,
    val accruedInterest: Float?,
    val debitCardsItems: List<BtpDebitCardsItem>?,
    val accountHolderNames: String?,
    val minimumRequiredBalance: Float?,
    val accountHolderAddressLine1: String?,
    val town: String?,
    val postCode: String?,
    val creditAccount: Boolean?,
    val debitAccount: Boolean?,
    val accountHolderCountry: String?,
    val unmaskableAttributes: List<String>?,
) : java.io.Serializable, Comparable<BtpProductItem> {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )

    override fun toString(): String {
        return "BtpProductItem{ id: $id, name: $name, displayName: $displayName }"
    }

    fun getAvailableBalanceAsFloat(): Float {
        var result = 0f
        availableBalance ?. let {
            try {
                result = it.toFloat()
            } catch (e: java.lang.NumberFormatException) {
                Log.w(
                    "BtpProductItem",
                    "Unexpected NumberFormatException converting available balance.",
                )
            }
        }
        return result
    }

    override fun compareTo(other: BtpProductItem): Int {
        return 0 // TODO
    }

    fun areItemsSame(other: BtpProductItem): Boolean {
        return id == other.id // TODO
    }

    fun areContentsSame(other: BtpProductItem): Boolean {
        return id == other.id // TODO
    }
}
