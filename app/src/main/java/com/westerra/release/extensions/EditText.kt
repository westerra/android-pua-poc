package com.westerra.release.extensions

import android.widget.EditText
import java.math.BigDecimal
import java.math.RoundingMode

fun EditText.getAmount(): BigDecimal {
    return try {
        val cleanString = text.replace("[$,.]".toRegex(), "")
        BigDecimal(cleanString)
            .setScale(2, RoundingMode.FLOOR)
            .divide(BigDecimal(100), RoundingMode.FLOOR)
    } catch (e: java.lang.NumberFormatException) {
        BigDecimal(0)
    }
}
