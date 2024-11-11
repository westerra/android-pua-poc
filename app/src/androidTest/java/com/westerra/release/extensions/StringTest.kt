package com.westerra.release.extensions

import com.westerra.release.accountandtransactions.CustomTransactionsUseCase
import java.time.format.DateTimeParseException
import org.junit.Assert
import org.junit.Test

class StringTest {
    private val dateTimeString1 = "2023-02-28T19:00:00"
    private val dateTimeString2 = "2007-12-03T10:15:30"
    private val badString1 = "2007-12-03T10:15:30Z"
    private val badString2 = "2007-12-03"
    private val accountNumber1 = "1234567890"
    private val accountNumber2 = "1234"
    private val accountNumber3 = "0"
    private val accountNumber4 = ""
    private val accountNumber5 = "asdfg"

    @Test
    fun string_extensions_test() {
        Assert.assertEquals("Feb 28, 2023", dateTimeString1.fromLocalDateTimeFormatToMDYString())
        Assert.assertEquals("Dec 03, 2007", dateTimeString2.fromLocalDateTimeFormatToMDYString())
        Assert.assertEquals("••••••7890", accountNumber1.maskAccountNumber())
        Assert.assertEquals(accountNumber2, accountNumber2.maskAccountNumber())
        Assert.assertEquals(accountNumber3, accountNumber3.maskAccountNumber())
        Assert.assertEquals(accountNumber4, accountNumber4.maskAccountNumber())
        Assert.assertEquals("•sdfg", accountNumber5.maskAccountNumber())
        Assert.assertTrue(
            CustomTransactionsUseCase.TRANSACTION_MANAGER_URL_ENDPOINT.appendToBaseUrl().endsWith(
                "/api/transaction-manager/client-api/v2/transactions"
            )
        )
    }

    @Test(expected = DateTimeParseException::class)
    fun string_extensions_parse_failure_test1() {
        badString1.fromLocalDateTimeFormatToMDYString()
    }

    @Test(expected = DateTimeParseException::class)
    fun string_extensions_parse_failure_test2() {
        badString2.fromLocalDateTimeFormatToMDYString()
    }
}
