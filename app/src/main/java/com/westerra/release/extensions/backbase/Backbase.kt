package com.westerra.release.extensions.backbase

import android.content.Context
import com.backbase.android.Backbase
import com.backbase.android.identity.client.BBIdentityAuthClient
import com.backbase.android.identity.device.BBDeviceAuthenticator
import com.backbase.android.identity.fido.passcode.BBPasscodeAuthenticator
import com.backbase.android.identity.otp.BBOtpAuthenticator
import com.backbase.android.identity.view.DefaultBBOtpAuthenticatorView
import com.backbase.android.identity.view.DefaultBBPasscodeAuthenticatorView

/*
  https://backbase.io/developers/documentation/identity-mobile-sdk/5.6/developer-guides/configure-identity-auth-client/
  Code to use a custom Identity AuthClient.
  Needed to try using this to do a few different things but never ended up using the solution.
  Also, don't think it ever worked fully when trying to use it.
  Keeping code around for the next time we need to investigate using a custom AuthClient
  as part of a solution.
*/
fun Backbase.useCustomAuthClient(context: Context) {
    val authClient = BBIdentityAuthClient(context, "").apply {
        addAuthenticator(BBDeviceAuthenticator())
        addAuthenticator(
            BBPasscodeAuthenticator(DefaultBBPasscodeAuthenticatorView::class.java),
        )
        addAuthenticator(
            BBOtpAuthenticator(DefaultBBOtpAuthenticatorView::class.java),
        )
    }
    this.registerAuthClient(authClient)
}
