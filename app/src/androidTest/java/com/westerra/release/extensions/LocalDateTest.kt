package com.westerra.release.extensions

import com.westerra.release.constants.Constants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import org.junit.Assert
import org.junit.Test

class LocalDateTest {

    private val localDate1 = LocalDate.of(2020, 1, 1)
    private val localDate2 = LocalDate.of(2022, 12, 25)

    @Test
    fun localDate_extensions_test() {
        Assert.assertEquals("Jan 01, 2020", localDate1.toMDYString())
        Assert.assertEquals("Dec 25, 2022", localDate2.toMDYString())
        Assert.assertEquals("2020-01-01", localDate1.toYMDString())
        Assert.assertEquals("2022-12-25", localDate2.toYMDString())
        Assert.assertEquals("Wednesday, Jan 01, 2020", localDate1.toPreferredDatePattern())
        Assert.assertEquals("Sunday, Dec 25, 2022", localDate2.toPreferredDatePattern())
    }

    @Test
    fun getCurrentLocalDate_test() {
        val currentLocalDate = getCurrentLocalDate()
        val now = Date()
        val dateFormatter = SimpleDateFormat(Constants.SIMPLE_YMD_PATTERN)
        Assert.assertEquals(dateFormatter.format(now), currentLocalDate.toYMDString())
    }
}
