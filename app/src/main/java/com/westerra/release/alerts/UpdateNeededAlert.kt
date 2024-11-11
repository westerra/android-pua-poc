package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.westerra.release.R
import com.westerra.release.constants.Constants
import com.westerra.release.firebase.model.MinimumVersionConfig

class UpdateNeededAlert(activity: Activity, val config: MinimumVersionConfig) :
    AlertDialog.Builder(activity) {

    init {
        setCancelable(false)
        if (config.minTitle == null) {
            setTitle(R.string.update_required_title)
        } else {
            setTitle(config.minTitle)
        }
        if (config.minMessage == null) {
            setMessage(R.string.update_required_message)
        } else {
            setMessage(config.minMessage)
        }
        setPositiveButton(context.getString(R.string.update_now)) { _, _ ->
            openPlayStore(context = activity)
        }
    }

    override fun show(): AlertDialog {
        val dialog = super.show()
        // Override here prevents the dialog from dismissing when button pressed.
        // If user returns to app, they are stuck until app is updated.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            openPlayStore(context = context)
        }
        return dialog
    }

    private fun openPlayStore(context: Context) {
        val appPackageName = Constants.PLAY_STORE_ID
        val marketIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appPackageName"),
        )
        val playStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"),
        )
        try {
            context.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(playStoreIntent)
        }
    }
}
