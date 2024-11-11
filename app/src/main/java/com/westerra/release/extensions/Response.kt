package com.westerra.release.extensions

import com.backbase.android.utils.net.response.Response

fun Response.WafFailure(): Response {
    errorMessage = "Authentication Failure"
    responseCode = 403
    statusText = "Authentication Failure"
    return this
}
