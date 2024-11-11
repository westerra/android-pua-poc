package com.westerra.release.btp.model

data class BtpAdditionItem(
    val allowFromP2P: String?,
    val allowTransferTo: String?,
    val accountCode: String?,
    val allowToP2P: String?,
    val allowTransferFrom: String?,
    val allowToA2A: String?,
    val isPrimaryBillpayAccount: String?,
    val allowFromA2A: String?,
    val payverisTransferTo: String?,
    val allowToBILLPAY: String?,
    val allowFromBILLPAY: String?,
    val payverisTransferFrom: String?,
) : java.io.Serializable, Comparable<BtpAdditionItem> {
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
    )

    override fun toString(): String {
        return "BtpAdditionItem{}"
    }

    override fun compareTo(other: BtpAdditionItem): Int {
        return 0 // TODO
    }

    fun areItemsSame(other: BtpAdditionItem): Boolean {
        return false // TODO
    }

    fun areContentsSame(other: BtpAdditionItem): Boolean {
        return false // TODO
    }
}
