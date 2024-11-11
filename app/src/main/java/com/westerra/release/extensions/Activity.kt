package com.westerra.release.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.westerra.release.alerts.ErrorAlert

fun Activity.launchExternalBrowser(url: String?) {
    if (url == null) {
        Log.d("Activity.launchExternalBrowser", "Invalid url")
        ErrorAlert(activity = this).show()
        return
    }
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toValidUri()))
        this.startActivity(browserIntent)
    } catch (e: Exception) {
        Log.d("Activity.launchExternalBrowser", e.message ?: "Unknown error", e)
        ErrorAlert(activity = this).show()
    }
}

// https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}
