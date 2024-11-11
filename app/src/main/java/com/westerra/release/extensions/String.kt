package com.westerra.release.extensions

import android.util.Log
import com.backbase.android.Backbase
import com.backbase.deferredresources.DeferredText
import com.westerra.release.constants.Constants
import com.westerra.release.constants.Tags.BACKBASE
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun String?.fromLocalDateTimeFormatToMDYString(): String {
    return OffsetDateTime.of(LocalDateTime.parse(this), ZoneOffset.UTC).toMDYString()
}

fun String?.fromLocalDateTimeFormatToPreferredDateString(): String {
    return OffsetDateTime.of(LocalDateTime.parse(this), ZoneOffset.UTC).toPreferredDateString()
}

fun String.maskAccountNumber(): String {
    return if (this.length > 4) {
        val rangeToReplace = 0.until(this.length - 4)
        (this as CharSequence).replaceRange(
            rangeToReplace,
            (Constants.ACCOUNT_NUMBER_MASK as CharSequence).repeat(rangeToReplace.last + 1),
        ).toString()
    } else {
        this
    }
}

fun String.appendToBaseUrl(): String {
    Backbase.getInstance() ?. let {
        return it.configuration.experienceConfiguration.serverURL.plus(this)
    }
    Log.e(BACKBASE, "No Backbase instance found. Backbase server url will likely fail.")
    return "https://127.0.0.1".plus(this)
}

fun String.appendToDSBaseUrl(): String {
    return this.appendToBaseUrl().replace("edge.", "digitalservices.")
}

fun String.toBackbaseCustomString(): String {
    return Backbase.getInstance()?.configuration?.custom?.get(this) as String
}

fun String.toDeferredText(): DeferredText {
    return DeferredText.Constant(this)
}

fun String.parseVersionComponents(): List<Int> {
    var cleaned = this
    val splitPostFix = this.split("-")
    if (splitPostFix.isNotEmpty()) {
        cleaned = splitPostFix.first()
    }
    val results = cleaned.split(".")
    try {
        return results.map { it.toInt() }
    } catch (e: NumberFormatException) {
        Log.e("String.parseVersionComponents", "Unexpected non-integer component.")
    }
    return listOf()
}

fun String.compareVersion(otherVersion: String): Boolean {
    val myVersions = this.parseVersionComponents()
    val requiredVersions = otherVersion.parseVersionComponents()
    if (myVersions.count() != 3 || requiredVersions.count() != 3) {
        Log.e("MinimumVersionConfig", "Unexpected version string.")
        return false
    }
    if (myVersions[0] < requiredVersions[0]) {
        return true
    } else if (myVersions[0] > requiredVersions[0]) {
        return false
    }
    if (myVersions[1] < requiredVersions[1]) {
        return true
    } else if (myVersions[1] > requiredVersions[1]) {
        return false
    }
    return (myVersions[2] < requiredVersions[2])
}

fun String.isValidUri(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://")
}

fun String.toValidUri(): String {
    return if (this.isValidUri()) this else "http://$this"
}
