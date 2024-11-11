package com.westerra.release.btp.model

// TODO: example api call was empty json.
data class BtpAggregatedBalanceItem(
    val todo: String?,
) : java.io.Serializable, Comparable<BtpAggregatedBalanceItem> {
    constructor() : this(null)

    override fun toString(): String {
        return "BtpAggregatedBalanceItem{ TODO: $todo }"
    }

    override fun compareTo(other: BtpAggregatedBalanceItem): Int {
        return 0 // TODO
    }

    fun areItemsSame(other: BtpAggregatedBalanceItem): Boolean {
        return false // TODO
    }

    fun areContentsSame(other: BtpAggregatedBalanceItem): Boolean {
        return false // TODO
    }
}
