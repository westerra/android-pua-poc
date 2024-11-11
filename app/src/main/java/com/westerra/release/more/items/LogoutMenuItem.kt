package com.westerra.release.more.items

import android.os.Looper
import androidx.core.os.bundleOf
import com.backbase.android.identity.journey.authentication.AuthenticationJourney
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.alerts.LogoutAlert
import com.westerra.release.alerts.LogoutAlertResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LogoutMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.log_out_title,
        icon = R.drawable.ic_signout,
        actionBlock = {
            if (Looper.getMainLooper().isCurrentThread) {
                when (showLogoutAlert()) {
                    LogoutAlertResult.LOGOUT -> OnActionComplete.NavigateTo(
                        R.id.action_mainScreen_to_authenticationJourney,
                        bundleOf(
                            AuthenticationJourney.LAUNCH_ACTION_END_SESSION to true,
                        ),
                    )
                    LogoutAlertResult.CHANGE_ACCOUNT -> OnActionComplete.NavigateTo(
                        R.id.action_mainScreen_to_authenticationJourney,
                        bundleOf(
                            AuthenticationJourney.LAUNCH_ACTION_LOG_OUT to true,
                        ),
                    )
                    LogoutAlertResult.CANCEL -> OnActionComplete.DoNothing
                }
            } else {
                OnActionComplete.DoNothing
            }
        },
    )

    private suspend fun showLogoutAlert() = suspendCoroutine { continuation ->
        val activity = WesterraApplication.getInstance().getActivity() ?: return@suspendCoroutine
        LogoutAlert(
            activity = activity,
            onLogOut = {
                continuation.resume(value = LogoutAlertResult.LOGOUT)
            },
            onChangeAccount = {
                continuation.resume(value = LogoutAlertResult.CHANGE_ACCOUNT)
            },
            onCancel = {
                continuation.resume(value = LogoutAlertResult.CANCEL)
            },
        ).show()
    }
}
