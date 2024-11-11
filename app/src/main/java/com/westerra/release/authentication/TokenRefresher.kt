package com.westerra.release.authentication

import com.backbase.android.Backbase
import com.backbase.android.clients.auth.oauth2.OAuth2AuthClient
import com.backbase.android.clients.auth.oauth2.OAuth2AuthClientListener
import com.backbase.android.modules.SessionState
import com.backbase.android.utils.net.response.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * To use add TokenRefresher.checkIfRefreshNeeded() to an event listener
 *
 * Example:
 * MainActivity {
 *      ...
 *      override fun onUserInteraction() {
 *           super.onUserInteraction()
 *           lifecycleScope.launch(Dispatchers.IO) {
 *           TokenRefresher.checkIfRefreshNeeded()
 *           }
 *       }
 *       ...
 * }
 *
 * Idle timer is not used by default but is meant to perform and action after a period of being inactive
 * To use call initializeIdleActivityTimer and pass onComplete action
 * Example:
 * MainActivity {
 *       ...
 *       TokenRefresher.initializeIdleActivityTimer {
 *           runOnUiThread {
 *              findNavController().navigate(
 *                  R.id.action_mainScreen_to_authenticationJourney, bundleOf(
 *                      AuthenticationJourney.LAUNCH_ACTION_END_SESSION to true
 *                  )
 *              )
 *       }
 *       ...
 *   }
 */

object TokenRefresher {
    val tokenScope = CoroutineScope(Dispatchers.IO)

    /**
     * set to number of seconds for delay between refreshing token
     */
    private const val MIN_SECONDS_REFRESH: Long = (30 * 1)

    /**
     * seconds idle before log out
     * after we get token we use the token expiration
     */
    private const val START_IDLE_TIME_LIMIT = 120L

    /**
     * This is meant to add time between refreshing tokens
     */
    private val refreshDelayTimer = WaitingTimer(MIN_SECONDS_REFRESH)

    /**
     * Set to true to run handle idle user activity
     */
    var useIdleActivity: Boolean = false

    /**
     * optional timer to perform action when user is inactive for a period of time
     */
    var idleActivityTimer: WaitingTimer? = null

    /**
     * Backbase Auth client
     */
    val client = (Backbase.getInstance()?.authClient as OAuth2AuthClient)

    /**
     * Parser class for jwt
     */
    var jwToken: JWToken? = null

    private var userHasInteracted: Boolean = false

    init {
        refreshDelayTimer.start()
        refreshDelayTimer.onTick = {
            // BBLogger.info(this.toString(), "${it.toString()} Seconds left for refreshDelay timer")
        }
    }

    /**
     * oauth listener Override if action needed
     */
    private var oauthListener = object : OAuth2AuthClientListener {
        override fun oAuth2AuthClientAccessTokenRefreshFailed(p0: Response) {}
        override fun oAuth2AuthClientAccessTokenRefreshed(
            p0: MutableMap<String, MutableList<String>>,
        ) {
            if (useIdleActivity && idleActivityTimer !== null) {
                parseToken(client.authTokens?.get("Authorization")!!)
                idleActivityTimer?.durationInSeconds = jwToken!!.secondsUntilExpired
                idleActivityTimer?.restart()
            }
        }
        override fun oAuth2AuthClientTokenInvalidated() {}
    }

    fun checkIfRefreshNeeded() {
        idleActivityTimer?.restart()
        userHasInteracted = true
        if (refreshDelayTimer.isRunning) return

        checkSessionValidity()
    }

    fun checkSessionValidity(sessionIsNone: () -> Unit = {}) {
        client.checkSessionValidity { session: SessionState ->
            if (session != SessionState.NONE && client.authTokens?.get("Authorization") != null) {
                parseToken(client.authTokens?.get("Authorization")!!)
                if (shouldRefresh) {
                    refresh()
                    refreshDelayTimer.restart()
                    userHasInteracted = false
                }
            } else {
                sessionIsNone()
            }
        }
    }

    private fun refresh() {
        client.refreshAccessToken(oauthListener, null, null)
    }

    private fun parseToken(token: String) {
        jwToken = JWToken(token)
    }

    private val shouldRefresh: Boolean
        get() = !jwToken!!.isExpired && (jwToken!!.secondsSinceIssued > MIN_SECONDS_REFRESH)

    fun initializeIdleActivityTimer(onComplete: (() -> Unit) = {}) {
        useIdleActivity = true
        idleActivityTimer = if (jwToken !== null && jwToken?.secondsUntilExpired !== null) {
            WaitingTimer(jwToken!!.secondsUntilExpired)
        } else {
            WaitingTimer(START_IDLE_TIME_LIMIT)
        }

        // make sure we have a token before we log the user out
        idleActivityTimer?.onComplete = {
            if (userHasInteracted) {
                refresh()
                userHasInteracted = false
            } else if (client.isTokenStored.name !== "NONE") {
                onComplete()
            }
            idleActivityTimer?.restart()
        }

        // To debug add onTick
        idleActivityTimer?.onTick = {
            // BBLogger.info(this.toString(), "${it.toString()} Seconds left for idleActivity timer")
        }

        idleActivityTimer?.start()
    }

    class WaitingTimer(_durationInSeconds: Long, _onComplete: (() -> Unit) = {}) {
        var durationInSeconds = _durationInSeconds
        var secondsRemaining: Long = durationInSeconds
        var onComplete: (() -> Unit) = _onComplete
        var onTick: ((Long) -> Unit) = {}
        val isRunning: Boolean get() = job != null && job!!.isActive
        var job: Job? = null

        fun start() {
            secondsRemaining = durationInSeconds
            job = tokenScope.launch {
                while (secondsRemaining > 0) {
                    delay(1000)
                    onTick(secondsRemaining)
                    --secondsRemaining
                    if (secondsRemaining < 1L) {
                        onComplete()
                        job?.cancelAndJoin()
                        job = null
                    }
                }
            }
        }

        fun restart() {
            when (isRunning) {
                true -> secondsRemaining = durationInSeconds
                false -> start()
            }
        }
    }
}
