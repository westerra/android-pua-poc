package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import com.westerra.release.R
import com.westerra.release.awswaf.WesterraWafToken
import com.westerra.release.custom.MySharedPreferences

enum class LogoutAlertResult {
    LOGOUT,
    CHANGE_ACCOUNT,
    CANCEL,
}

class LogoutAlert(
    activity: Activity,
    onLogOut: () -> Unit,
    onChangeAccount: () -> Unit,
    onCancel: () -> Unit,
) :
    AlertDialog.Builder(activity) {

    init {
        setCancelable(true)
        setTitle(R.string.log_out_message)
        setPositiveButton(context.getString(R.string.cancel)) { dialog, _ ->
            onCancel()
            dialog.dismiss()
        }
        val items = mutableListOf<String>()
        items.add(context.getString(R.string.log_out_title))
        items.add(context.getString(R.string.switch_account_title))
        setItems(items.toTypedArray()) { _, which ->
            when (which) {
                0 -> {
                    onLogOut()
                    WesterraWafToken.refreshToken()
                }
                1 -> {
                    onChangeAccount()
                    WesterraWafToken.refreshToken()
                    MySharedPreferences.enableInterceptAdvertisement()
                    MySharedPreferences.resetMemberNumberState()
                }
            }
        }

        setOnCancelListener { dialog ->
            onCancel()
            dialog.dismiss()
        }
    }
}
