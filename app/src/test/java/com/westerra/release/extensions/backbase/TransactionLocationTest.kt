package com.westerra.release.extensions.backbase

import com.backbase.android.client.transactionclient2.model.TransactionLocation
import java.math.BigDecimal
import org.junit.Assert
import org.junit.Test

class TransactionLocationTest {
    private val loc1 = TransactionLocation {
        id = 1
        address = null
        latitude = null
        longitude = null
    }
    private val loc2 = TransactionLocation {
        id = 2
        address = ""
        latitude = BigDecimal.ZERO
        longitude = BigDecimal.ZERO
    }
    private val loc3 = TransactionLocation {
        id = 3
        address = "3700 E Alameda Ave, Denver, CO 80209"
        latitude = 39.71129557968322.toBigDecimal()
        longitude = (-104.94340575856323).toBigDecimal()
    }

    @Test
    fun transactionLocation_isValid() {
        Assert.assertFalse(loc1.isValid())
        Assert.assertFalse(loc2.isValid())
        Assert.assertTrue(loc3.isValid())
    }

    @Test
    fun transactionLocation_toGeoCoordinates() {
        Assert.assertNull(loc1.toGeoCoordinates())
        Assert.assertNull(loc2.toGeoCoordinates())
        Assert.assertNotNull(loc3.toGeoCoordinates())
    }
}
