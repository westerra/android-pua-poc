package com.westerra.release.extensions

import android.icu.text.NumberFormat
import java.math.BigDecimal
import java.util.Locale

fun BigDecimal?.toUSDString(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}
