package com.westerra.release.extensions

import com.westerra.release.constants.Constants
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime?.toMDYString(): String {
    return this?.toLocalDate()?.format(
        DateTimeFormatter.ofPattern(
            Constants.SIMPLE_MDY_PATTERN,
        ),
    ).toString()
}

fun OffsetDateTime?.toPreferredDateString(): String {
    return this?.toLocalDate()?.format(
        DateTimeFormatter.ofPattern(
            Constants.PREFERRED_DATE_PATTERN,
        ),
    ).toString()
}
