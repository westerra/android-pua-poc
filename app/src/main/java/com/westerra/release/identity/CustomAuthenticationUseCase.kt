package com.westerra.release.identity

import android.util.Log
import com.backbase.android.identity.journey.authentication.AuthenticationUseCase
import com.westerra.release.analytics.WesterraAnalytics

class CustomAuthenticationUseCase(
    private val defaultUseCase: AuthenticationUseCase,
) : AuthenticationUseCase {
    companion object {
        private val TAG = CustomAuthenticationUseCase::class.java.simpleName
    }
    override suspend fun authenticateWithDevice(
        username: String,
    ): AuthenticationUseCase.AuthenticateResult {
        if (WafReadySignal.useDefaultAuthentication()) {
            val result = defaultUseCase.authenticateWithDevice(username = username)
            if (result == AuthenticationUseCase.AuthenticateResult.Success) {
                WesterraAnalytics.recordDeviceLoginEvent()
            }
            return result
        }
        return makeWafFailureResult()
    }

    override suspend fun authenticateWithPassword(
        username: CharArray,
        password: CharArray,
    ): AuthenticationUseCase.AuthenticateResult {
        if (WafReadySignal.useDefaultAuthentication()) {
            val result = defaultUseCase.authenticateWithPassword(
                username = username,
                password = password,
            )
            if (result == AuthenticationUseCase.AuthenticateResult.Success) {
                WesterraAnalytics.recordPasswordLoginEvent()
            }
            return result
        }
        return makeWafFailureResult()
    }

    override suspend fun changePasscode(
        username: CharArray,
    ): AuthenticationUseCase.AuthenticateResult {
        return defaultUseCase.changePasscode(username)
    }

    override suspend fun completeRegistration(
        username: CharArray,
    ): AuthenticationUseCase.AuthenticateResult {
        return defaultUseCase.completeRegistration(username)
    }

    override suspend fun endSession() {
        defaultUseCase.endSession()
    }

    override suspend fun forgotPasscode(
        username: CharArray,
        password: CharArray,
    ): AuthenticationUseCase.AuthenticateResult {
        return defaultUseCase.forgotPasscode(username, password)
    }

    override suspend fun forgotPassword(): AuthenticationUseCase.AuthenticateResult {
        return defaultUseCase.forgotPassword()
    }

    override suspend fun forgotUsername(): AuthenticationUseCase.AuthenticateResult {
        if (WafReadySignal.useDefaultAuthentication()) {
            return defaultUseCase.forgotUsername()
        }
        return makeWafFailureResult()
    }

    override suspend fun getUserInfo(): AuthenticationUseCase.UserInfo? {
        return defaultUseCase.getUserInfo()
    }

    override fun isBiometricRegistered(username: String): Boolean {
        return defaultUseCase.isBiometricRegistered(username = username)
    }

    override fun isDeviceRegistered(): Boolean {
        return defaultUseCase.isDeviceRegistered()
    }

    override fun isPasscodeRegistered(username: String): Boolean {
        return defaultUseCase.isPasscodeRegistered(username = username)
    }

    override suspend fun logOut() {
        defaultUseCase.logOut()
    }

    private fun makeWafFailureResult(): AuthenticationUseCase.AuthenticateResult {
        Log.d(TAG, "authenticateWithPassword: WAF Authentication Expired")
        return WafFailure.init()
    }
}
