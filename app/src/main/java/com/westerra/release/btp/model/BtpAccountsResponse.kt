package com.westerra.release.btp.model

import com.westerra.release.sso.model.NetworkResponse

data class BtpAccountsResponse(
    override var errorMessage: String?,
    override var error: String? = null,
    var currentAccounts: BtpAccountItem? = null,
    var savingsAccounts: BtpAccountItem? = null,
    var termDeposits: BtpAccountItem? = null,
    var loans: BtpAccountItem? = null,
    var creditCards: BtpAccountItem? = null,
    var debitCards: BtpAccountItem? = null,
    var investmentAccounts: BtpAccountItem? = null,
    var status: Int? = null,
) : NetworkResponse() {
    fun getInternalTransferAccounts(): List<BtpProductItem> {
        val response = mutableListOf<BtpProductItem>()
        currentAccounts?.products ?. let {
            response.addAll(it)
        }
        savingsAccounts?.products ?. let {
            response.addAll(it)
        }
        termDeposits?.products ?. let {
            response.addAll(it)
        }
        loans?.products ?. let {
            response.addAll(it)
        }
        creditCards?.products ?. let {
            response.addAll(it)
        }
        debitCards?.products ?. let {
            response.addAll(it)
        }
        investmentAccounts?.products ?. let {
            response.addAll(it)
        }
        return response.toList()
    }

    fun countAccountsForProduct(productTypeName: String?): Int {
        if (productTypeName == null) return 0
        var result = 0
        result += currentAccounts?.countProducts(productTypeName) ?: 0
        result += savingsAccounts?.countProducts(productTypeName) ?: 0
        result += termDeposits?.countProducts(productTypeName) ?: 0
        result += loans?.countProducts(productTypeName) ?: 0
        result += creditCards?.countProducts(productTypeName) ?: 0
        result += debitCards?.countProducts(productTypeName) ?: 0
        result += investmentAccounts?.countProducts(productTypeName) ?: 0
        return result
    }
}
