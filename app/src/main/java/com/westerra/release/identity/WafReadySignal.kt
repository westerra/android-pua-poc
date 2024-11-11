package com.westerra.release.identity

object WafReadySignal {
    var isTokenAvailable = false
    var isRemotelyEnabled = true

    fun useDefaultAuthentication(): Boolean {
        return isRemotelyEnabled && isTokenAvailable
    }
}
