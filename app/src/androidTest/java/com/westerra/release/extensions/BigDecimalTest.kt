package com.westerra.release.extensions

import java.math.BigDecimal
import org.junit.Assert
import org.junit.Test

class BigDecimalTest {

    private val bigDecimal1 = BigDecimal("0.0")
    private val bigDecimal2 = BigDecimal("0.0000009")
    private val bigDecimal3 = BigDecimal("1000000000.100000")
    private val bigDecimal4 = BigDecimal("1000000000.99999999")

    @Test
    fun bigDecimal_extensions_test() {
        Assert.assertEquals("$0.00", bigDecimal1.toUSDString())
        Assert.assertEquals("$0.00", bigDecimal2.toUSDString())
        Assert.assertEquals("$1,000,000,000.10", bigDecimal3.toUSDString())
        Assert.assertEquals("$1,000,000,001.00", bigDecimal4.toUSDString())
    }
}
