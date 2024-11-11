package com.westerra.release.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.BuildConfig
import com.backbase.android.identity.journey.authentication.AuthenticationJourney
import com.backbase.android.identity.journey.authentication.initAuthenticationJourney
import com.backbase.android.identity.journey.authentication.stopAuthenticationJourney
import com.backbase.android.retail.journey.app.us.UsAppActivity
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.authentication.TokenRefresher
import com.westerra.release.firebase.RemoteConfigProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WesterraActivity : UsAppActivity() {

    override val graphResId: Int
        get() = R.navigation.westerra_app

    companion object {
        private const val AUTHENTICATION_JOURNEY_LABEL = "Authentication Journey"
    }

    private lateinit var analytics: WesterraAnalytics

    init {
        WesterraApplication.getInstance().setActivity(this@WesterraActivity)
        RemoteConfigProvider().setupFirebaseRemoteConfig(activity = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = WesterraAnalytics()
        findNavController().addOnDestinationChangedListener(analytics)
        initializeSessionTimeout()
        initAuthenticationJourney()
        if (BuildConfig.DEBUG) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        // Backbase.requireInstance().useCustomAuthClient(context = applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAuthenticationJourney()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        lifecycleScope.launch(Dispatchers.IO) {
            TokenRefresher.checkIfRefreshNeeded()
        }
        WesterraAnalytics.incrementBtpClickCount()
    }

    override fun onRestart() {
        super.onRestart()
        if (findNavController().currentDestination?.label.toString()
            != AUTHENTICATION_JOURNEY_LABEL
        ) {
            TokenRefresher.checkSessionValidity(
                sessionIsNone = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        logoutAndNavigate()
                    }
                },
            )
        } else {
            // TODO
        }
    }

    private fun initializeSessionTimeout() {
        TokenRefresher.initializeIdleActivityTimer {
            runOnUiThread { logoutAndNavigate() }
        }
    }

    private fun logoutAndNavigate() {
        val bundle = bundleOf(
            AuthenticationJourney.LAUNCH_ACTION_END_SESSION to true,
            AuthenticationJourney.LAUNCH_ALERT_TITLE to getString(R.string.session_expired_title),
            AuthenticationJourney.LAUNCH_ALERT_MESSAGE to getString(
                R.string.please_login_again_message,
            ),
        )
        findNavController().navigate(
            R.id.action_global_authenticationJourney,
            bundle,
        )

        TokenRefresher.idleActivityTimer?.restart()
    }
}
