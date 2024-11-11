package com.westerra.release.identity

import com.backbase.android.identity.journey.authentication.AuthenticationUseCase.AuthenticateResult.Failure
import com.backbase.android.identity.journey.common.data.ResponseData
import com.backbase.android.utils.net.response.Response
import com.westerra.release.extensions.WafFailure

object WafFailure {
    fun init(): Failure {
        return Failure(
            cause = Failure.Cause.AuthenticationFailed(
                message = "Firewall not enabled",
                responseData = ResponseData(Response().WafFailure()),
            ),
        )
    }
}
