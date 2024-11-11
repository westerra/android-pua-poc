package com.westerra.release.btp.model

data class BtpAccountItem(
    val name: String?,
    val aggregatedBalance: BtpAggregatedBalanceItem?,
    val products: List<BtpProductItem>?,
) : java.io.Serializable, Comparable<BtpAccountItem> {
    constructor() : this(null, null, null)

    override fun toString(): String {
        return "AccountsItem{ name: $name, products: $products }"
    }

    override fun compareTo(other: BtpAccountItem): Int {
        return compareDisplayNames(other = other)
    }

    private fun compareDisplayNames(other: BtpAccountItem): Int {
        return getDisplayName().compareTo(other.getDisplayName())
    }

    fun getDisplayName(): String {
        return name ?: ""
    }

    fun areItemsSame(other: BtpAccountItem): Boolean {
        return name == other.name
    }

    fun areContentsSame(other: BtpAccountItem): Boolean {
        return name == other.name
    }

    fun countProducts(productTypeName: String): Int {
        return products?.filter { productItem ->
            productItem.productTypeName == productTypeName
        }?.size ?: 0
    }
}
