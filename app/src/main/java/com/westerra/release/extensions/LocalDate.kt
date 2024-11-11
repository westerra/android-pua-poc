package com.westerra.release.extensions

import com.westerra.release.constants.Constants
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

fun LocalDate?.toMDYString(): String {
    return this?.format(
        DateTimeFormatter.ofPattern(Constants.SIMPLE_MDY_PATTERN),
    ).toString()
}

fun LocalDate?.toPreferredDatePattern(): String {
    return this?.format(
        DateTimeFormatter.ofPattern(Constants.PREFERRED_DATE_PATTERN),
    ).toString()
}

fun LocalDate?.toYMDString(): String {
    return this?.format(
        DateTimeFormatter.ofPattern(Constants.SIMPLE_YMD_PATTERN),
    ).toString()
}

fun LocalDate?.toDate(): Date {
    return Date.from(this?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
}

fun getCurrentLocalDate(): LocalDate {
    val calendar = Calendar.getInstance()
    calendar.time = Date()

    return LocalDate.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH),
    )
}
