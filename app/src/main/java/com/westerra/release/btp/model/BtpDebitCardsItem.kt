package com.westerra.release.btp.model

// TODO: example api had no values
data class BtpDebitCardsItem(
    val todo: String?,
) : java.io.Serializable, Comparable<BtpDebitCardsItem> {
    constructor() : this(null)

    override fun toString(): String {
        return "BtpDebitCardsItem{ TODO: $todo }"
    }

    override fun compareTo(other: BtpDebitCardsItem): Int {
        return 0 // TODO
    }

    fun areItemsSame(other: BtpDebitCardsItem): Boolean {
        return false // TODO
    }

    fun areContentsSame(other: BtpDebitCardsItem): Boolean {
        return false // TODO
    }
}
