package com.westerra.release.extensions

import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.junit.Assert
import org.junit.Test

class OffsetDateTimeTest {

    private val offsetDateTime1 = OffsetDateTime.of(2020, 6, 15, 11, 30, 0, 0, ZoneOffset.UTC)
    private val offsetDateTime2 = OffsetDateTime.of(2022, 1, 1, 12, 30, 30, 900, ZoneOffset.UTC)

    @Test
    fun offsetDateTime_extensions_test() {
        Assert.assertEquals("Jun 15, 2020", offsetDateTime1.toMDYString())
        Assert.assertEquals("Jan 01, 2022", offsetDateTime2.toMDYString())
    }
}
