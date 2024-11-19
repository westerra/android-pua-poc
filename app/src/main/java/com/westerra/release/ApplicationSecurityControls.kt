package com.westerra.release

import android.app.Application
import com.backbase.android.Backbase
import com.backbase.android.core.errorhandling.ErrorCodes
import com.backbase.android.core.security.environment.EnvironmentVerification
import com.backbase.android.listeners.SecurityViolationListener
import com.backbase.android.utils.net.response.Response

/**
 * Convenience method to be invoked from [Application.onCreate] if security controls are wished
 * to be enabled.
 *
 * Note: Only for the non-debug variant of the application.
 */
internal fun Application.enableSecurityControls() {
    if (BuildConfig.DEBUG) return

    val environmentVerification = EnvironmentVerification.getInstance()

    val isEmulator = environmentVerification.getEmulatorDetector(this).isEmulator
    if (isEmulator) crashApp()

    val isRooted = environmentVerification.getRootVerification(this)
        .withRootManagementApps()
        .withPotentiallyDangerousApps()
        .withSuBinary()
        .isRooted
    if (isRooted) crashApp()

 /*   TODO- ActivityHijackDetector is deprecated, Refer https://backbase.io/documentation/mobile-sdk/3.10/activity_hijacking
       */
   // environmentVerification.activityHijackDetector.start(this)
}

/**
 * Convenience method to be invoked from [onInitializeDependencies] if anti native debugging
 * is wished to be enabled. This method requires [Backbase] instance to be initialized.
 *
 * Note: Only for the non-debug variant of the application.
 */
internal fun enableAntiNativeDebug() {
    if (BuildConfig.DEBUG) return
    EnvironmentVerification.getInstance().debuggerDetector.enableAntiNativeDebug()
}

/**
 * Convenience method to be invoked from [onInitializeDependencies] if security violation
 * listener is wished to be enabled. This method requires [Backbase] instance to be initialized.
 *
 * Note: Only for the non-debug variant of the application.
 */
internal fun setSecurityViolationListener() {
    if (BuildConfig.DEBUG) return
    Backbase.requireInstance().setSecurityViolationListener(SecurityViolationHandler())
}

private fun crashApp(code: Int = 1): Nothing = throw RuntimeException("$code")

private class SecurityViolationHandler : SecurityViolationListener {
    override fun onSecurityViolation(violation: Response) {
        when (val responseCode = violation.responseCode) {
          //  ErrorCodes.SECURITY_BREACH_ACTIVITY_HIJACKING.code -> crashApp(responseCode)
        }
    }
}
