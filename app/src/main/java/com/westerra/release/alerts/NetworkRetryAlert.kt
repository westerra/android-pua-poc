package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import com.westerra.release.R

class NetworkRetryAlert(
    activity: Activity,
    onRetry: () -> Unit,
) : AlertDialog.Builder(activity) {
    init {
        setCancelable(true)
        setTitle(R.string.network_failure_title)
        setMessage(R.string.network_failure_message)
        setPositiveButton(R.string.retry) { _, _ ->
            onRetry()
        }
        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
    }
}
