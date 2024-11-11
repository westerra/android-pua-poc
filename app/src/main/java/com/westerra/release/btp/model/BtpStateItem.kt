package com.westerra.release.btp.model

data class BtpStateItem(
    val externalStateId: String?,
    val state: String?,
) : java.io.Serializable, Comparable<BtpStateItem> {
    constructor() : this(null, null)

    override fun toString(): String {
        return "BtpStateItem{ externalStateId: $externalStateId, state: $state }"
    }

    override fun compareTo(other: BtpStateItem): Int {
        return 0 // TODO
    }

    fun areItemsSame(other: BtpStateItem): Boolean {
        return false // TODO
    }

    fun areContentsSame(other: BtpStateItem): Boolean {
        return false // TODO
    }
}
