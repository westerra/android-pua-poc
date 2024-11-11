package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import com.westerra.release.R

class ErrorAlert(
    activity: Activity,
) : AlertDialog.Builder(activity) {
    init {
        setCancelable(true)
        setTitle(R.string.error_title)
        setMessage(R.string.oops_error_message)
        setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
    }
}
