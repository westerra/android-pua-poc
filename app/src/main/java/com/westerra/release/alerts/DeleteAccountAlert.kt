package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import com.westerra.release.R
import com.westerra.release.constants.Constants.WESTERRA_CALL_NUMBER
import com.westerra.release.databinding.AlertDeleteAccountBinding

class DeleteAccountAlert(activity: Activity) : AlertDialog.Builder(activity) {
    init {
        val binding = AlertDeleteAccountBinding.inflate(activity.layoutInflater)
        binding.phoneButton.setOnClickListener {
            callSupport(activity = activity)
        }
        setView(binding.contents)
        setCancelable(true)
        setTitle(R.string.account_deletion_title)
        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun callSupport(activity: Activity) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$WESTERRA_CALL_NUMBER")
        activity.startActivity(intent)
    }
}
